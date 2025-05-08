package hei.school.prog3.dto.RestMapper;

import hei.school.prog3.dto.ClubRest.ClubSimpleRequest;
import hei.school.prog3.dto.ClubRest.ClubResponse;
import hei.school.prog3.dto.ClubRest.ClubWithoutPlayerListResponse;
import hei.school.prog3.dto.CoachRest.CoachResponse;
import hei.school.prog3.model.Club;
import hei.school.prog3.dto.ClubRest.ClubStatistics;
import hei.school.prog3.dto.ClubRest.ClubStatisticsRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClubRestMapper  {
    @Autowired private CoachRestMapper coachRestMapper;

    public ClubResponse toRest(Club club) {
        CoachResponse coachResponse = coachRestMapper.toRest(club.getCoach());
        return new ClubResponse(
                club.getId(),
                club.getName(),
                club.getAcronym(),
                club.getYearCreation(),
                club.getStadium(),
                coachResponse
        );
    }
    public Club toModel(ClubSimpleRequest clubSimpleRequest){
        Club club = new Club();
        club.setId(clubSimpleRequest.getId());
        club.setName(clubSimpleRequest.getName());
        club.setAcronym(clubSimpleRequest.getAcronym());
        club.setYearCreation(clubSimpleRequest.getYearCreation());
        club.setStadium(clubSimpleRequest.getStadium());
        club.setCoach(coachRestMapper.toModel(clubSimpleRequest.getCoach()));
        return club;
    }

    public ClubWithoutPlayerListResponse toClubWithoutPlayerListResponse(Club club) {
        if (club == null) {
            return null;
        }
        return new ClubWithoutPlayerListResponse(
                club.getId(),
                club.getName(),
                club.getAcronym(),
                club.getYearCreation(),
                club.getStadium(),
                coachRestMapper.toRest(club.getCoach())
        );
    }

    public ClubStatisticsRest toClubStatisticsRest(ClubStatistics clubStatistics) {
        if (clubStatistics == null) {
            return null;
        }
        ClubStatisticsRest clubStatisticsRest = new ClubStatisticsRest();
        clubStatisticsRest.setClub(toClubWithoutPlayerListResponse(clubStatistics.getClub()));
        clubStatisticsRest.setRankingPoints(clubStatistics.getRankingPoints());
        clubStatisticsRest.setScoredGoals(clubStatistics.getScoredGoals());
        clubStatisticsRest.setConcededGoals(clubStatistics.getConcededGoals());
        clubStatisticsRest.setDifferenceGoals(clubStatistics.getDifferenceGoals());
        clubStatisticsRest.setCleanSheetNumber(clubStatistics.getCleanSheetNumber());
        return clubStatisticsRest;
    }
}
