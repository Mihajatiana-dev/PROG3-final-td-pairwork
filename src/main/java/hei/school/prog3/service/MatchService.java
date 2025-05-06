package hei.school.prog3.service;

import hei.school.prog3.api.RestMapper.MatchConverter;
import hei.school.prog3.api.dto.request.AddGoal;
import hei.school.prog3.dao.operations.GoalDAO;
import hei.school.prog3.dao.operations.MatchDAO;
import hei.school.prog3.dao.operations.PlayerDAO;
import hei.school.prog3.dao.operations.SeasonDAO;
import hei.school.prog3.model.*;
import hei.school.prog3.model.enums.MatchStatus;
import hei.school.prog3.model.enums.SeasonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchDAO matchDAO;
    private final MatchConverter matchConverter;
    private final SeasonDAO seasonDAO;
    private final GoalDAO goalDAO;
    private final PlayerDAO playerDAO;

    public List<MatchMinimumInfo> createAll(int seasonYear) {
        // find season by year
        Season season = seasonDAO.findByYear(seasonYear)
                .orElseThrow(() -> new IllegalArgumentException("Seaoson not found for the year: " + seasonYear));

        // verify if season is not started
        if (season.getStatus() != SeasonStatus.NOT_STARTED) {
            throw new IllegalStateException("Season should have NOT_STARTED status");
        }

        // generate matches for season
        List<Match> matchesWithInfo = matchDAO.createSeasonMatches(UUID.fromString(season.getId()));

        // convert to Match
        return matchConverter.convertAll(matchesWithInfo);
    }

    public List<MatchMinimumInfo> getFilteredMatches(int seasonYear, List<FilterCriteria> filters, int page, int size) {
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("Page and size must be positive values");
        }

        // get filtered matches
        List<Match> matchesWithInfo = matchDAO.findBySeasonAndFilters(
                seasonYear,
                filters,
                page,
                size);

        // For matches that have started or finished, retrieve and set scorer information
        for (Match match : matchesWithInfo) {
            if (match.getActualStatus() == MatchStatus.STARTED || match.getActualStatus() == MatchStatus.FINISHED) {
                // Retrieve goals for the match
                List<Goal> goals = goalDAO.findByMatchId(match.getId());

                // Separate goals by home and away club
                List<Scorer> homeScorers = new ArrayList<>();
                List<Scorer> awayScorers = new ArrayList<>();

                for (Goal goal : goals) {
                    // Create a Scorer object from the Goal
                    Player player = playerDAO.findById(goal.getPlayerId());
                    if (player != null) {
                        PlayerMinimumInfo playerInfo = new PlayerMinimumInfo(
                            player.getId(),
                            player.getName(),
                            player.getNumber()
                        );

                        Scorer scorer = new Scorer(
                            playerInfo,
                            goal.getMinute(),
                            goal.getOwnGoal()
                        );

                        // For own goals, add the scorer to their own club's list, not the opponent's
                        if (goal.getOwnGoal()) {
                            // Add to the player's own club list
                            if (player.getClub().getId().equals(match.getHomeClub().getId())) {
                                homeScorers.add(scorer);
                            } else if (player.getClub().getId().equals(match.getAwayClub().getId())) {
                                awayScorers.add(scorer);
                            }
                        } else {
                            // For regular goals, add to the club that the goal is attributed to
                            if (goal.getClubId().equals(match.getHomeClub().getId())) {
                                homeScorers.add(scorer);
                            } else if (goal.getClubId().equals(match.getAwayClub().getId())) {
                                awayScorers.add(scorer);
                            }
                        }
                    }
                }

                // Set the scorers on the match
                match.setHomeScorers(homeScorers);
                match.setAwayScorers(awayScorers);
            }
        }

        // convert
        return matchConverter.convertAll(matchesWithInfo);
    }

    public MatchMinimumInfo updateMatchStatus(String matchId, MatchStatus newStatus) {
        // Get the current match
        Match currentMatch = matchDAO.findMatchById(matchId);
        if (currentMatch == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Error: Match not found. The match with ID " + matchId + " does not exist in the database.");
        }

        // Get the current status
        MatchStatus currentStatus = currentMatch.getActualStatus();

        // Validate the status transition
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Error: Invalid status transition. Cannot change status from " + currentStatus + " to " + newStatus + ". Valid transitions are: NOT_STARTED -> STARTED, STARTED -> FINISHED");
        }

        // Check if we need to set matchDatetime (only when transitioning from NOT_STARTED to STARTED)
        boolean setMatchDatetime = (currentStatus == MatchStatus.NOT_STARTED && newStatus == MatchStatus.STARTED);

        // Update the status
        Match updatedMatch = matchDAO.updateMatchStatus(matchId, newStatus, setMatchDatetime);
        if (updatedMatch == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error: Failed to update match status. Please try again later.");
        }

        // Convert
        return matchConverter.convert(updatedMatch);
    }

    private boolean isValidStatusTransition(MatchStatus currentStatus, MatchStatus newStatus) {
        // NOT_STARTED > STARTED > FINISHED
        if (currentStatus == MatchStatus.NOT_STARTED && newStatus == MatchStatus.STARTED) {
            return true;
        } else if (currentStatus == MatchStatus.STARTED && newStatus == MatchStatus.FINISHED) {
            return true;
        } else if (currentStatus == newStatus) {
            return false;
        }
        return false;
    }

    public MatchMinimumInfo addGoals(String matchId, List<AddGoal> goalsToAdd) {
        // Get the current match
        Match match = matchDAO.findMatchById(matchId);
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Error: Match not found. The match with ID " + matchId + " does not exist in the database.");
        }

        // Validate match status
        if (match.getActualStatus() != MatchStatus.STARTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Error: Invalid match status. Goals can only be added to matches with status STARTED. Current match status is " + match.getActualStatus() + ".");
        }

        // Process each goal
        List<Goal> goals = new ArrayList<>();
        for (AddGoal goalRequest : goalsToAdd) {
            // Validate club is participating in the match
            String clubId = goalRequest.getClubId();
            if (!isClubParticipatingInMatch(match, clubId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Error: Invalid club. The club with ID " + clubId + " is not participating in this match. Only clubs with IDs " + 
                    match.getHomeClub().getId() + " (" + match.getHomeClub().getName() + ") and " + 
                    match.getAwayClub().getId() + " (" + match.getAwayClub().getName() + ") are allowed.");
            }

            // Get player
            String playerId = goalRequest.getScorerIdentifier();

            // Validate player is participating in the match
            if (!isPlayerParticipatingInMatch(match, playerId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Error: Invalid player. The player with ID " + playerId + " is not participating in this match. " +
                    "Only players from clubs with IDs " + match.getHomeClub().getId() + " (" + match.getHomeClub().getName() + ") and " + 
                    match.getAwayClub().getId() + " (" + match.getAwayClub().getName() + ") are allowed to score goals.");
            }

            // Determine if it's an own goal
            boolean isOwnGoal = isOwnGoal(playerId, clubId);

            // Create goal
            Goal goal = new Goal();
            goal.setPlayerId(playerId);
            goal.setClubId(clubId);
            goal.setMatchId(matchId);
            goal.setMinute(goalRequest.getMinuteOfGoal());
            goal.setOwnGoal(isOwnGoal);

            goals.add(goal);
        }

        // Save goals
        goalDAO.saveAll(goals);

        // Update match score
        goalDAO.updateMatchScore(matchId);

        // Get updated match
        Match updatedMatch = matchDAO.findMatchById(matchId);

        // Get all goals for the match
        List<Goal> allGoals = goalDAO.findByMatchId(matchId);

        // Convert goals to scorers and set them on the match
        List<Scorer> homeScorers = new ArrayList<>();
        List<Scorer> awayScorers = new ArrayList<>();

        for (Goal goal : allGoals) {
            // Get player information
            Player player = playerDAO.findById(goal.getPlayerId());
            if (player != null) {
                // Create player minimum info
                PlayerMinimumInfo playerInfo = new PlayerMinimumInfo();
                playerInfo.setId(player.getId());
                playerInfo.setName(player.getName());
                playerInfo.setNumber(player.getNumber());

                // Create scorer
                Scorer scorer = new Scorer();
                scorer.setPlayer(playerInfo);
                scorer.setMinuteOfGoal(goal.getMinute());
                scorer.setOwnGoal(goal.getOwnGoal());

                // For own goals, add the scorer to their own club's list, not the opponent's
                if (goal.getOwnGoal()) {
                    // Add to the player's own club list
                    if (player.getClub().getId().equals(updatedMatch.getHomeClub().getId())) {
                        homeScorers.add(scorer);
                    } else if (player.getClub().getId().equals(updatedMatch.getAwayClub().getId())) {
                        awayScorers.add(scorer);
                    }
                } else {
                    // For regular goals, add to the club that the goal is attributed to
                    if (goal.getClubId().equals(updatedMatch.getHomeClub().getId())) {
                        homeScorers.add(scorer);
                    } else if (goal.getClubId().equals(updatedMatch.getAwayClub().getId())) {
                        awayScorers.add(scorer);
                    }
                }
            }
        }

        // Set scorers on the match
        updatedMatch.setHomeScorers(homeScorers);
        updatedMatch.setAwayScorers(awayScorers);

        // Convert and return
        return matchConverter.convert(updatedMatch);
    }

    private boolean isClubParticipatingInMatch(Match match, String clubId) {
        return match.getHomeClub().getId().equals(clubId) || match.getAwayClub().getId().equals(clubId);
    }

    private boolean isOwnGoal(String playerId, String clubId) {
        try {
            // Get player's club
            Player player = playerDAO.findById(playerId);
            if (player == null || player.getClub() == null) {
                return false;
            }

            // If player's club is different from the club the goal is attributed to, it's an own goal
            return !player.getClub().getId().equals(clubId);
        } catch (Exception e) {
            // If there's an error, assume it's not an own goal
            return false;
        }
    }

    private boolean isPlayerParticipatingInMatch(Match match, String playerId) {
        try {
            // Get player's club
            Player player = playerDAO.findById(playerId);
            if (player == null || player.getClub() == null) {
                return false;
            }

            // Check if player's club is one of the clubs participating in the match
            String playerClubId = player.getClub().getId();
            return match.getHomeClub().getId().equals(playerClubId) || 
                   match.getAwayClub().getId().equals(playerClubId);
        } catch (Exception e) {
            // If there's an error, assume player is not participating
            return false;
        }
    }
}
