package hei.school.prog3.api.RestMapper;

import hei.school.prog3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MatchConverter {

    public Match convertToMatch(MatchWithAllInformations matchWithInfo) {
        Match match = new Match();
        match.setId(matchWithInfo.getId());
        match.setStadium(matchWithInfo.getStadium());
        match.setMatchDatetime(matchWithInfo.getMatchDatetime());
        match.setActualStatus(matchWithInfo.getActualStatus());

        // Conversion du club domicile
        MatchClub homeMatchClub = new MatchClub();
        homeMatchClub.setClub(convertToClubMinimumInfo(matchWithInfo.getHomeClub()));
        homeMatchClub.setClubScore(createEmptyClubScore()); // Score vide par défaut
        match.setClubPlayingHome(homeMatchClub);

        // Conversion du club extérieur
        MatchClub awayMatchClub = new MatchClub();
        awayMatchClub.setClub(convertToClubMinimumInfo(matchWithInfo.getAwayClub()));
        awayMatchClub.setClubScore(createEmptyClubScore()); // Score vide par défaut
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

    public List<Match> convertAll(List<MatchWithAllInformations> matchesWithInfo) {
        return matchesWithInfo.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public Match convert(MatchWithAllInformations matchWithInfo) {
        Match match = new Match();
        match.setId(matchWithInfo.getId());
        match.setStadium(matchWithInfo.getStadium());
        match.setMatchDatetime(matchWithInfo.getMatchDatetime());
        match.setActualStatus(matchWithInfo.getActualStatus());

        match.setClubPlayingHome(createMatchClub(matchWithInfo.getHomeClub()));
        match.setClubPlayingAway(createMatchClub(matchWithInfo.getAwayClub()));

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
