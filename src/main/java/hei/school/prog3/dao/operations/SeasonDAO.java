package hei.school.prog3.dao.operations;

import hei.school.prog3.api.dto.request.CreateSeason;
import hei.school.prog3.config.DbConnection;
import hei.school.prog3.exception.BadRequestException;
import hei.school.prog3.exception.NotFoundException;
import hei.school.prog3.model.Season;
import hei.school.prog3.model.enums.SeasonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SeasonDAO implements GenericOperations<Season> {
    private final DbConnection dataSource;

    @Override
    public List<Season> showAll(int page, int size) {
        return List.of();
    }

    @Override
    public List<Season> save(List<Season> seasons) {
        return List.of();
    }

    @Override
    public Season findById(int modelId) {
        return null;
    }

    public List<Season> getAllSeasons() {
        List<Season> seasons = new ArrayList<>();
        String sql = "SELECT season_id, year, alias, status FROM season";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Season season = new Season();
                season.setYear(rs.getInt("year"));
                season.setAlias(rs.getString("alias"));
                season.setId(rs.getObject("season_id", UUID.class).toString());
                season.setStatus(SeasonStatus.valueOf(rs.getString("status")));
                seasons.add(season);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch seasons", e);
        }
        return seasons;
    }

    public List<Season> createSeasons(List<CreateSeason> seasonsToCreate) {
        List<Season> createdSeasons = new ArrayList<>();
        String sql = "INSERT INTO season (year, alias) VALUES (?, ?) RETURNING season_id, year, alias, status";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            for (CreateSeason season : seasonsToCreate) {
                pstm.setInt(1, season.getYear());
                pstm.setString(2, season.getAlias());

                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        Season created = new Season();
                        created.setId(rs.getObject("season_id", UUID.class).toString());
                        created.setYear(rs.getInt("year"));
                        created.setAlias(rs.getString("alias"));
                        created.setStatus(SeasonStatus.valueOf(rs.getString("status")));
                        createdSeasons.add(created);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create seasons", e);
        }
        return createdSeasons;
    }

    public Season updateSeasonStatus(int year, SeasonStatus newStatus) {
        String selectSql = "SELECT status FROM season WHERE year = ?";
        String updateSql = "UPDATE season SET status = ?::season_status_enum WHERE year = ? RETURNING season_id, year, alias, status";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectSql);
             PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {

            selectStmt.setInt(1, year);
            SeasonStatus currentStatus = null;

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (!rs.next()) {
                    throw new NotFoundException("Season not found for year: " + year);
                }
                currentStatus = SeasonStatus.valueOf(rs.getString("status"));
            }

            // verify the order
            if (!isValidTransition(currentStatus, newStatus)) {
                throw new BadRequestException("Invalid status transition from " + currentStatus + " to " + newStatus);
            }

            // update
            updateStmt.setString(1, newStatus.toString());
            updateStmt.setInt(2, year);

            try (ResultSet rs = updateStmt.executeQuery()) {
                if (rs.next()) {
                    Season updated = new Season();
                    updated.setId(rs.getObject("season_id", UUID.class).toString());
                    updated.setYear(rs.getInt("year"));
                    updated.setAlias(rs.getString("alias"));
                    updated.setStatus(SeasonStatus.valueOf(rs.getString("status")));
                    return updated;
                }
                throw new RuntimeException("Update failed for season year: " + year);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while updating season status", e);
        }
    }

    private boolean isValidTransition(SeasonStatus current, SeasonStatus newStatus) {
        if (current == SeasonStatus.NOT_STARTED && newStatus != SeasonStatus.STARTED) {
            throw new BadRequestException(
                    "Impossible de passer de NOT_STARTED à " + newStatus + ". Seule la transition vers STARTED est autorisée."
            );
        }
        if (current == SeasonStatus.STARTED && newStatus != SeasonStatus.FINISHED) {
            throw new BadRequestException(
                    "Impossible de passer de STARTED à " + newStatus + ". Seule la transition vers FINISHED est autorisée."
            );
        }
        if (current == SeasonStatus.FINISHED) {
            throw new BadRequestException(
                    "Impossible de modifier une saison FINISHED."
            );
        }
        return true;
    }
}
