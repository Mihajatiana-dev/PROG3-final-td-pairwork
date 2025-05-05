package hei.school.prog3.dao.operations;

import hei.school.prog3.api.RestMapper.MatchConverter;
import hei.school.prog3.api.RestMapper.MatchMapper;
import hei.school.prog3.config.DbConnection;
import hei.school.prog3.model.*;
import hei.school.prog3.model.enums.MatchStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MatchDAO implements GenericOperations<Match> {
    private final DbConnection dataSource;
    private final ClubDAO clubDAO;
    private final MatchMapper matchMapper;
    private final MatchConverter matchConverter;

    @Override
    public List<Match> showAll(int page, int size) {
        return List.of();
    }

    @Override
    public List<Match> save(List<Match> matches) {
        return List.of();
    }

    @Override
    public Match findById(String modelId) {
        MatchWithAllInformations matchWithInfo = findMatchById(modelId);
        if (matchWithInfo == null) {
            return null;
        }
        return matchConverter.convert(matchWithInfo);
    }

    public MatchWithAllInformations findMatchById(String matchId) {
        String sql = "SELECT m.match_id, m.stadium, m.match_datetime, m.status, " +
                "home.club_id as home_club_id, home.club_name as home_club_name, " +
                "home.acronym as home_acronym, away.club_id as away_club_id, " +
                "away.club_name as away_club_name, away.acronym as away_acronym, " +
                "s.season_id, s.alias, s.year, s.status as season_status " +
                "FROM match m " +
                "JOIN club home ON m.home_club_id = home.club_id " +
                "JOIN club away ON m.away_club_id = away.club_id " +
                "JOIN season s ON m.season_id = s.season_id " +
                "WHERE m.match_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, UUID.fromString(matchId));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return matchMapper.mapMatch(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching match with ID: " + matchId, e);
        }

        return null;
    }

    public List<MatchWithAllInformations> createSeasonMatches(UUID seasonId) {
        // Vérifier que la saison est bien NOT_STARTED
        if (!isSeasonNotStarted(seasonId)) {
            throw new IllegalArgumentException("La saison doit avoir le statut NOT_STARTED");
        }

        // Récupérer tous les clubs
        List<Club> clubs = getAllClubs();
        if (clubs.size() < 2) {
            throw new IllegalStateException("Au moins 2 clubs sont nécessaires pour créer des matchs");
        }

        List<MatchWithAllInformations> matches = new ArrayList<>();

        // Créer les matchs aller-retour pour chaque paire de clubs
        for (int i = 0; i < clubs.size(); i++) {
            for (int j = i + 1; j < clubs.size(); j++) {
                Club homeClub = clubs.get(i);
                Club awayClub = clubs.get(j);

                // Match aller
                MatchWithAllInformations homeMatch = createMatch(
                        homeClub,
                        awayClub,
                        homeClub.getStadium(),
                        null, // matchDatetime = null
                        seasonId
                );
                matches.add(homeMatch);

                // Match retour
                MatchWithAllInformations awayMatch = createMatch(
                        awayClub,
                        homeClub,
                        awayClub.getStadium(),
                        null, // matchDatetime = null
                        seasonId
                );
                matches.add(awayMatch);
            }
        }

        return matches;
    }

    private boolean isSeasonNotStarted(UUID seasonId) {
        String sql = "SELECT status FROM season WHERE season_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, seasonId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                return "NOT_STARTED".equals(status);
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification du statut de la saison", e);
        }
    }

    private List<Club> getAllClubs() {
        List<Club> clubs = new ArrayList<>();
        String sql = "SELECT c.club_id, c.club_name, c.acronym, c.year_creation, c.stadium, " +
                "co.coach_id, co.coach_name, co.nationality as coach_nationality " +
                "FROM club c JOIN coach co ON c.coach_id = co.coach_id";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Coach coach = new Coach(
                        rs.getString("coach_id"),
                        rs.getString("coach_name"),
                        rs.getString("coach_nationality")
                );

                Club club = new Club(
                        rs.getString("club_id"),
                        rs.getString("club_name"),
                        rs.getString("acronym"),
                        rs.getInt("year_creation"),
                        rs.getString("stadium"),
                        coach
                );

                clubs.add(club);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des clubs", e);
        }

        return clubs;
    }

    private MatchWithAllInformations createMatch(Club homeClub, Club awayClub, String stadium,
                                                 LocalDateTime matchDatetime, UUID seasonId) {
        String sql = "INSERT INTO match (home_club_id, away_club_id, stadium, match_datetime, season_id) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING match_id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, UUID.fromString(homeClub.getId()));
            stmt.setObject(2, UUID.fromString(awayClub.getId()));
            stmt.setString(3, stadium);

            // Gestion du matchDatetime null
            if (matchDatetime != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(matchDatetime));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.setObject(5, seasonId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String matchId = rs.getString("match_id");

                // Création de l'objet Season avec les informations minimales
                Season season = new Season();
                season.setId(seasonId.toString());

                return new MatchWithAllInformations(
                        matchId,
                        homeClub,
                        awayClub,
                        stadium,
                        null, // matchDatetime = null
                        MatchStatus.NOT_STARTED,
                        season
                );
            }
            throw new RuntimeException("Échec de la création du match");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du match", e);
        }
    }

    public MatchWithAllInformations updateMatchStatus(String matchId, MatchStatus newStatus) {
        String sql = "UPDATE match SET status = ?::match_status_enum WHERE match_id = ? RETURNING match_id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newStatus.toString());
            statement.setObject(2, UUID.fromString(matchId));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return findMatchById(matchId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating match status for ID: " + matchId, e);
        }

        return null;
    }

    public List<MatchWithAllInformations> findBySeasonAndFilters(int seasonYear,
                                                                 List<FilterCriteria> filterCriteriaList,
                                                                 int page,
                                                                 int size) {
        if (page < 1) {
            throw new IllegalArgumentException("Page must be greater than 0 but actual is " + page);
        }

        List<MatchWithAllInformations> matches = new ArrayList<>();

        String sql = "SELECT m.match_id, m.stadium, m.match_datetime, m.status, " +
                "home.club_id as home_club_id, home.club_name as home_club_name, " +
                "home.acronym as home_acronym, away.club_id as away_club_id, " +
                "away.club_name as away_club_name, away.acronym as away_acronym, " +
                "s.season_id, s.alias, s.year, s.status as season_status " +
                "FROM match m " +
                "JOIN club home ON m.home_club_id = home.club_id " +
                "JOIN club away ON m.away_club_id = away.club_id " +
                "JOIN season s ON m.season_id = s.season_id " +
                "WHERE s.year = ?";

        List<String> conditions = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        values.add(seasonYear);

        for (FilterCriteria filterCriteria : filterCriteriaList) {
            String column = filterCriteria.getColumn();
            Object value = filterCriteria.getValue();

            if ("status".equals(column)) {
                conditions.add("m.status = ?::match_status_enum");
                values.add(value.toString());
            } else if ("clubName".equals(column)) {
                conditions.add("(home.club_name ILIKE ? OR away.club_name ILIKE ?)");
                values.add("%" + value + "%");
                values.add("%" + value + "%");
            } else if ("matchAfter".equals(column)) {
                conditions.add("m.match_datetime > ?");
                values.add(Timestamp.valueOf((LocalDateTime) value));
            } else if ("matchBeforeOrEquals".equals(column)) {
                conditions.add("m.match_datetime <= ?");
                values.add(Timestamp.valueOf((LocalDateTime) value));
            }
        }

        if (!conditions.isEmpty()) {
            sql += " AND " + String.join(" AND ", conditions);
        }

        sql += " LIMIT ? OFFSET ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int index = 1;
            for (Object value : values) {
                statement.setObject(index++, value);
            }
            statement.setInt(index++, size);
            statement.setInt(index, size * (page - 1));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    MatchWithAllInformations match = matchMapper.mapMatch(resultSet);
                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching matches", e);
        }

        return matches;
    }
}
