package hei.school.prog3.api.RestMapper;

import hei.school.prog3.model.Club;
import hei.school.prog3.model.Match;
import hei.school.prog3.model.Season;
import hei.school.prog3.model.enums.MatchStatus;
import hei.school.prog3.model.enums.SeasonStatus;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class MatchMapper {

    public Match mapMatch(ResultSet rs) throws SQLException {
        Match match = new Match();
        match.setId(rs.getString("match_id"));
        match.setStadium(rs.getString("stadium"));

        Timestamp timestamp = rs.getTimestamp("match_datetime");
        match.setMatchDatetime(timestamp != null ? timestamp.toLocalDateTime() : null);

        match.setActualStatus(MatchStatus.valueOf(rs.getString("status")));

        // Home Club
        Club homeClub = new Club(
                rs.getString("home_club_id"),
                rs.getString("home_club_name"),
                rs.getString("home_acronym"),
                null, // yearCreation pas nécessaire ici
                null, // stadium pas nécessaire ici
                null  // coach pas nécessaire ici
        );
        match.setHomeClub(homeClub);

        // Away Club
        Club awayClub = new Club(
                rs.getString("away_club_id"),
                rs.getString("away_club_name"),
                rs.getString("away_acronym"),
                null, // yearCreation pas nécessaire ici
                null, // stadium pas nécessaire ici
                null  // coach pas nécessaire ici
        );
        match.setAwayClub(awayClub);

        // Season
        Season season = new Season();
        season.setId(rs.getString("season_id"));
        season.setAlias(rs.getString("alias"));
        season.setYear(rs.getInt("year"));
        season.setStatus(SeasonStatus.valueOf(rs.getString("season_status")));
        match.setSeason(season);

        return match;
    }
}