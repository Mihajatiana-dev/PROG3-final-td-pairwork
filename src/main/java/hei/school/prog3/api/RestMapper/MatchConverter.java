package hei.school.prog3.api.RestMapper;

import hei.school.prog3.model.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MatchConverter {

    public MatchMinimumInfo convertToMatch(Match matchWithInfo) {
        MatchMinimumInfo match = new MatchMinimumInfo();
        match.setId(matchWithInfo.getId());
        match.setStadium(matchWithInfo.getStadium());
        match.setMatchDatetime(matchWithInfo.getMatchDatetime());
        match.setActualStatus(matchWithInfo.getActualStatus());

        // Conversion du club domicile with actual score and scorers
        MatchClub homeMatchClub = new MatchClub();
        homeMatchClub.setClub(convertToClubMinimumInfo(matchWithInfo.getHomeClub()));
        ClubScore homeClubScore = new ClubScore();
        homeClubScore.setScore(matchWithInfo.getHomeScore() != null ? matchWithInfo.getHomeScore() : 0);
        homeClubScore.setScorers(matchWithInfo.getHomeScorers() != null ? matchWithInfo.getHomeScorers() : Collections.emptyList());
        homeMatchClub.setClubScore(homeClubScore);
        match.setClubPlayingHome(homeMatchClub);

        // Conversion du club extérieur with actual score and scorers
        MatchClub awayMatchClub = new MatchClub();
        awayMatchClub.setClub(convertToClubMinimumInfo(matchWithInfo.getAwayClub()));
        ClubScore awayClubScore = new ClubScore();
        awayClubScore.setScore(matchWithInfo.getAwayScore() != null ? matchWithInfo.getAwayScore() : 0);
        awayClubScore.setScorers(matchWithInfo.getAwayScorers() != null ? matchWithInfo.getAwayScorers() : Collections.emptyList());
        awayMatchClub.setClubScore(awayClubScore);
        match.setClubPlayingAway(awayMatchClub);

        return match;
    }

    private ClubMinimumInfo convertToClubMinimumInfo(Club club) {
        ClubMinimumInfo clubMinInfo = new ClubMinimumInfo();
        clubMinInfo.setId(club.getId());
        clubMinInfo.setName(club.getName());
        clubMinInfo.setAcronym(club.getAcronym());
        return clubMinInfo;
    }

    private ClubScore createEmptyClubScore() {
        ClubScore clubScore = new ClubScore();
        clubScore.setScore(0); // Score initial à 0
        clubScore.setScorers(Collections.emptyList()); // Liste vide de buteurs
        return clubScore;
    }

    public List<MatchMinimumInfo> convertAll(List<Match> matchesWithInfo) {
        return matchesWithInfo.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public MatchMinimumInfo convert(Match matchWithInfo) {
        MatchMinimumInfo match = new MatchMinimumInfo();
        match.setId(matchWithInfo.getId());
        match.setStadium(matchWithInfo.getStadium());
        match.setMatchDatetime(matchWithInfo.getMatchDatetime());
        match.setActualStatus(matchWithInfo.getActualStatus());

        // Create MatchClub for home club with actual score and scorers
        MatchClub homeMatchClub = new MatchClub();
        homeMatchClub.setClub(convertToClubMinimumInfo(matchWithInfo.getHomeClub()));
        ClubScore homeClubScore = new ClubScore();
        homeClubScore.setScore(matchWithInfo.getHomeScore() != null ? matchWithInfo.getHomeScore() : 0);
        homeClubScore.setScorers(matchWithInfo.getHomeScorers() != null ? matchWithInfo.getHomeScorers() : Collections.emptyList());
        homeMatchClub.setClubScore(homeClubScore);
        match.setClubPlayingHome(homeMatchClub);

        // Create MatchClub for away club with actual score and scorers
        MatchClub awayMatchClub = new MatchClub();
        awayMatchClub.setClub(convertToClubMinimumInfo(matchWithInfo.getAwayClub()));
        ClubScore awayClubScore = new ClubScore();
        awayClubScore.setScore(matchWithInfo.getAwayScore() != null ? matchWithInfo.getAwayScore() : 0);
        awayClubScore.setScorers(matchWithInfo.getAwayScorers() != null ? matchWithInfo.getAwayScorers() : Collections.emptyList());
        awayMatchClub.setClubScore(awayClubScore);
        match.setClubPlayingAway(awayMatchClub);

        return match;
    }

    private MatchClub createMatchClub(Club club) {
        MatchClub matchClub = new MatchClub();
        matchClub.setClub(new ClubMinimumInfo(
                club.getId(),
                club.getName(),
                club.getAcronym()
        ));
        matchClub.setClubScore(new ClubScore(0, Collections.emptyList()));
        return matchClub;
    }
}
