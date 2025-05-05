package hei.school.prog3.dao.operations;

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
}
