package hei.school.prog3.service;

import hei.school.prog3.api.RestMapper.ClubRestMapper;
import hei.school.prog3.api.RestMapper.PlayerRestMapper;
import hei.school.prog3.api.dto.request.ClubSimpleRequest;
import hei.school.prog3.api.dto.request.CoachSimpleRequest;
import hei.school.prog3.api.dto.response.ClubResponse;
import hei.school.prog3.api.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.dao.operations.ClubDAO;
import hei.school.prog3.dao.operations.GoalDAO;
import hei.school.prog3.dao.operations.PlayerDAO;
import hei.school.prog3.exception.PlayerAlreadyAttachedException;
import hei.school.prog3.exception.PlayerInformationMismatchException;
import hei.school.prog3.exception.ResourceNotFoundException;
import hei.school.prog3.model.*;
import hei.school.prog3.model.enums.MatchStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubDAO clubDAO;
    private final PlayerDAO playerDAO;
    private final ClubRestMapper clubRestMapper;
    private final MatchService matchService;
    private final GoalDAO goalDAO;

    public List<Club> getAllClubResponses(int page, int size) {
        return clubDAO.showAll(page, size);
    }

    public List<Club> saveAllClubResponses(List<Club> clubToSave) {
        return clubDAO.saveAll(clubToSave);
    }

    public Club getClubWithPlayers(String clubId) {
        return clubDAO.getClubWithPlayers(clubId);
    }

    public List<Player> changePlayers(List<Player> playersToChange, String clubId) {
        // Verify club exists
        Club club = verifyExistingCLub(clubId);

        // Check if any player is already attached to another club and verify player information
        for (Player player : playersToChange) {
            // Check if player is attached to a club
            Club existingClub = clubDAO.findClubByPlayerId(player.getId());
            if (existingClub != null && existingClub.getId() != null) {
                throw new PlayerAlreadyAttachedException("Player with ID " + player.getId() + " is already attached to a club.");
            }

            // Verify player information
            Player existingPlayer = playerDAO.findById(player.getId());
            if (existingPlayer != null) {
                // Check if all player information matches
                if (!existingPlayer.getName().equals(player.getName()) ||
                        !existingPlayer.getNumber().equals(player.getNumber()) ||
                        !existingPlayer.getPosition().equals(player.getPosition()) ||
                        !existingPlayer.getNationality().equals(player.getNationality()) ||
                        !existingPlayer.getAge().equals(player.getAge())) {
                    throw new PlayerInformationMismatchException("Player information for ID " + player.getId() + " does not match existing player information.");
                }
            }
        }

        // If all players are not attached to any club and information is correct, proceed with changing players
        return clubDAO.changePlayers(clubId, playersToChange);
    }

    public Club verifyExistingCLub(String Id) {
        Club club = clubDAO.findById(Id);
        if (club == null || club.getId() == null) {
            throw new ResourceNotFoundException("Club with ID " + Id + " not found.");
        }
        return club;
    }

    public List<Player> addPlayerIntoCLub(String Id, List<Player> players) {
        //String Id == clubID
        Club club = verifyExistingCLub(Id);
        // Validate
        for (Player player : players) {
            Club existingClub = clubDAO.findClubByPlayerId(player.getId());
            if (existingClub != null && existingClub.getId() != null && !existingClub.getId().equals(club.getId())) {
                throw new PlayerAlreadyAttachedException("Player with ID " + player.getId() + " is already attached to another club.");
            }

            // Verify player information if player exists
            Player existingPlayer = playerDAO.findById(player.getId());
            if (existingPlayer != null) {
                // Check if all player information matches
                if (!existingPlayer.getName().equals(player.getName()) ||
                        !existingPlayer.getNumber().equals(player.getNumber()) ||
                        !existingPlayer.getPosition().equals(player.getPosition()) ||
                        !existingPlayer.getNationality().equals(player.getNationality()) ||
                        !existingPlayer.getAge().equals(player.getAge())) {
                    throw new PlayerInformationMismatchException("Player information for ID " + player.getId() + " does not match existing player information.");
                }
            }
        }
        // Insert
        for (Player player : players) {
            Player existingPlayer = playerDAO.findById(player.getId());
            if (existingPlayer == null) {
                // Create new player with club
                Player newPlayer = new Player();
                newPlayer.setId(player.getId());
                newPlayer.setName(player.getName());
                newPlayer.setNumber(player.getNumber());
                newPlayer.setPosition(player.getPosition());
                newPlayer.setNationality(player.getNationality());
                newPlayer.setAge(player.getAge());
                newPlayer.setClub(club);

                playerDAO.savePLayerWithoutUpdate(List.of(newPlayer));
            } else {
                // Player exists, just attach to club if not already attached to this club
                // The attachPlayerToClub method will only update if club_id is NULL or already equals this club's ID
                playerDAO.attachPlayerToClub(player.getId(), club.getId());
            }
        }
        Club updatedClub = clubDAO.getClubWithPlayers(Id);
        return updatedClub.getPlayers();
    }

    public List<ClubStatistics> getClubStatistics(int seasonYear, boolean hasToBeClassified) {
        // Get all clubs
        List<Club> allClubs = clubDAO.showAll(1, Integer.MAX_VALUE);

        // Get all matches for the season that are FINISHED
        List<FilterCriteria> filters = new ArrayList<>();
        filters.add(new FilterCriteria("status", MatchStatus.FINISHED));
        List<MatchMinimumInfo> finishedMatches = matchService.getFilteredMatches(seasonYear, filters, 1, Integer.MAX_VALUE);

        // Create a map to store statistics for each club
        Map<String, ClubStatistics> clubStatsMap = new HashMap<>();

        // Initialize statistics for all clubs
        for (Club club : allClubs) {
            ClubStatistics stats = new ClubStatistics();
            stats.setClub(club);
            stats.setRankingPoints(0);
            stats.setScoredGoals(0);
            stats.setConcededGoals(0);
            stats.setDifferenceGoals(0);
            stats.setCleanSheetNumber(0);

            clubStatsMap.put(club.getId(), stats);
        }

        // Calculate statistics based on matches
        for (MatchMinimumInfo match : finishedMatches) {
            String homeClubId = match.getClubPlayingHome().getClub().getId();
            String awayClubId = match.getClubPlayingAway().getClub().getId();

            // Get club statistics objects
            ClubStatistics homeStats = clubStatsMap.get(homeClubId);
            ClubStatistics awayStats = clubStatsMap.get(awayClubId);

            // Skip if either club doesn't exist in our map
            if (homeStats == null || awayStats == null) {
                continue;
            }

            // Get scores for ranking points calculation
            int homeScore = match.getClubPlayingHome().getClubScore() != null ? match.getClubPlayingHome().getClubScore().getScore() : 0;
            int awayScore = match.getClubPlayingAway().getClubScore() != null ? match.getClubPlayingAway().getClubScore().getScore() : 0;

            // Count goals scored by club players (including own goals from opponent players)
            int homeScoredGoals = 0;
            int awayScoredGoals = 0;

            // Count home team's scored goals (excluding own goals)
            if (match.getClubPlayingHome().getClubScore() != null && match.getClubPlayingHome().getClubScore().getScorers() != null) {
                for (Scorer scorer : match.getClubPlayingHome().getClubScore().getScorers()) {
                    if (scorer.getOwnGoal() == null || !scorer.getOwnGoal()) {
                        homeScoredGoals++;
                    }
                }
            }

            // Count away team's scored goals (excluding own goals)
            if (match.getClubPlayingAway().getClubScore() != null && match.getClubPlayingAway().getClubScore().getScorers() != null) {
                for (Scorer scorer : match.getClubPlayingAway().getClubScore().getScorers()) {
                    if (scorer.getOwnGoal() == null || !scorer.getOwnGoal()) {
                        awayScoredGoals++;
                    }
                }
            }

            // Count own goals by away team players and add them to home team's scored goals
            if (match.getClubPlayingAway().getClubScore() != null && match.getClubPlayingAway().getClubScore().getScorers() != null) {
                for (Scorer scorer : match.getClubPlayingAway().getClubScore().getScorers()) {
                    if (scorer.getOwnGoal() != null && scorer.getOwnGoal()) {
                        homeScoredGoals++;
                    }
                }
            }

            // Count own goals by home team players and add them to away team's scored goals
            if (match.getClubPlayingHome().getClubScore() != null && match.getClubPlayingHome().getClubScore().getScorers() != null) {
                for (Scorer scorer : match.getClubPlayingHome().getClubScore().getScorers()) {
                    if (scorer.getOwnGoal() != null && scorer.getOwnGoal()) {
                        awayScoredGoals++;
                    }
                }
            }

            // Update scored and conceded goals
            homeStats.setScoredGoals(homeStats.getScoredGoals() + homeScoredGoals);
            homeStats.setConcededGoals(homeStats.getConcededGoals() + awayScore);
            awayStats.setScoredGoals(awayStats.getScoredGoals() + awayScoredGoals);
            awayStats.setConcededGoals(awayStats.getConcededGoals() + homeScore);

            // Update ranking points
            if (homeScore > awayScore) {
                // Home win
                homeStats.setRankingPoints(homeStats.getRankingPoints() + 3);
            } else if (homeScore < awayScore) {
                // Away win
                awayStats.setRankingPoints(awayStats.getRankingPoints() + 3);
            } else {
                // Draw
                homeStats.setRankingPoints(homeStats.getRankingPoints() + 1);
                awayStats.setRankingPoints(awayStats.getRankingPoints() + 1);
            }

            // Update clean sheets
            if (awayScore == 0) {
                homeStats.setCleanSheetNumber(homeStats.getCleanSheetNumber() + 1);
            }
            if (homeScore == 0) {
                awayStats.setCleanSheetNumber(awayStats.getCleanSheetNumber() + 1);
            }
        }

        // Calculate difference goals for all clubs
        for (ClubStatistics stats : clubStatsMap.values()) {
            // Use Math.abs to ensure differenceGoals is always positive
            stats.setDifferenceGoals((stats.getScoredGoals() - stats.getConcededGoals()));
        }

        // Convert map to list
        List<ClubStatistics> result = new ArrayList<>(clubStatsMap.values());

        // Sort the list if required
        if (hasToBeClassified) {
            result.sort((a, b) -> {
                // 1. Ranking points (descending)
                int pointsComparison = Integer.compare(b.getRankingPoints(), a.getRankingPoints());
                if (pointsComparison != 0) {
                    return pointsComparison;
                }

                // 2. Difference goals (descending)
                int diffGoalsComparison = Integer.compare(b.getDifferenceGoals(), a.getDifferenceGoals());
                if (diffGoalsComparison != 0) {
                    return diffGoalsComparison;
                }

                // 3. Clean sheets (descending)
                return Integer.compare(b.getCleanSheetNumber(), a.getCleanSheetNumber());
            });
        } else {
            // Sort by club name (ascending)
            result.sort(Comparator.comparing(stats -> stats.getClub().getName()));
        }

        return result;
    }

    public List<ClubToFetch> getAllClubsWithStats() {
        List<Club> clubs = clubDAO.findAllClubs();
        return clubs.stream()
                .map(club -> {
                    ClubToFetch clubToFetch = new ClubToFetch();

                    // Convert club to ClubSimpleRequest
                    ClubSimpleRequest clubSimple = new ClubSimpleRequest();
                    clubSimple.setId(club.getId());
                    clubSimple.setName(club.getName());
                    clubSimple.setAcronym(club.getAcronym());
                    clubSimple.setYearCreation(club.getYearCreation());
                    clubSimple.setStadium(club.getStadium());

                    // Add coach if exist
                    if (club.getCoach() != null) {
                        CoachSimpleRequest coachSimple = new CoachSimpleRequest();
                        coachSimple.setName(club.getCoach().getName());
                        coachSimple.setNationality(club.getCoach().getNationality());
                        clubSimple.setCoach(coachSimple);
                    }

                    clubToFetch.setClub(clubSimple);

                    // get stats
                    int scoredGoals = clubDAO.getScoredGoals(club.getId());
                    int concededGoals = clubDAO.getConcededGoals(club.getId());

                    clubToFetch.setScoredGoals(scoredGoals);
                    clubToFetch.setConcededGoals(concededGoals);
                    clubToFetch.setDifferenceGoals(scoredGoals - concededGoals);
                    clubToFetch.setCleanSheetNumber(clubDAO.getCleanSheetNumber(club.getId()));

                    return clubToFetch;
                })
                .toList();
    }
}
