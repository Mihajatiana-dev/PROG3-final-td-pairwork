package hei.school.prog3.dao.operations;

import hei.school.prog3.config.DbConnection;
import hei.school.prog3.model.Goal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class GoalDAO {
    private final DbConnection dataSource;

    public Goal save(Goal goal) {
        String sql = "INSERT INTO goal (player_id, club_id, match_id, minute, own_goal) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING goal_id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, UUID.fromString(goal.getPlayerId()));
            statement.setObject(2, UUID.fromString(goal.getClubId()));
            statement.setObject(3, UUID.fromString(goal.getMatchId()));
            statement.setInt(4, goal.getMinute());
            statement.setBoolean(5, goal.getOwnGoal());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    goal.setId(resultSet.getString("goal_id"));
                    return goal;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving goal", e);
        }

        return null;
    }

    public List<Goal> saveAll(List<Goal> goals) {
        List<Goal> savedGoals = new ArrayList<>();
        for (Goal goal : goals) {
            Goal savedGoal = save(goal);
            if (savedGoal != null) {
                savedGoals.add(savedGoal);
            }
        }
        return savedGoals;
    }

    public List<Goal> findByMatchId(String matchId) {
        String sql = "SELECT goal_id, player_id, club_id, match_id, minute, own_goal " +
                "FROM goal WHERE match_id = ?";

        List<Goal> goals = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, UUID.fromString(matchId));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Goal goal = new Goal();
                    goal.setId(resultSet.getString("goal_id"));
                    goal.setPlayerId(resultSet.getString("player_id"));
                    goal.setClubId(resultSet.getString("club_id"));
                    goal.setMatchId(resultSet.getString("match_id"));
                    goal.setMinute(resultSet.getInt("minute"));
                    goal.setOwnGoal(resultSet.getBoolean("own_goal"));
                    goals.add(goal);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching goals for match: " + matchId, e);
        }

        return goals;
    }

    public void updateMatchScore(String matchId) {
        String sql = "WITH home_goals AS (" +
                "    SELECT COUNT(*) AS count FROM goal g " +
                "    JOIN match m ON g.match_id = m.match_id " +
                "    WHERE g.match_id = ? AND ((g.club_id = m.home_club_id AND g.own_goal = false) OR (g.club_id = m.home_club_id AND g.own_goal = true))" +
                "), " +
                "away_goals AS (" +
                "    SELECT COUNT(*) AS count FROM goal g " +
                "    JOIN match m ON g.match_id = m.match_id " +
                "    WHERE g.match_id = ? AND ((g.club_id = m.away_club_id AND g.own_goal = false) OR (g.club_id = m.away_club_id AND g.own_goal = true))" +
                ") " +
                "INSERT INTO match_score (match_id, home_score, away_score) " +
                "VALUES (?, (SELECT count FROM home_goals), (SELECT count FROM away_goals)) " +
                "ON CONFLICT (match_id) " +
                "DO UPDATE SET home_score = (SELECT count FROM home_goals), away_score = (SELECT count FROM away_goals)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            UUID matchUuid = UUID.fromString(matchId);
            statement.setObject(1, matchUuid);
            statement.setObject(2, matchUuid);
            statement.setObject(3, matchUuid);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating match score for match: " + matchId, e);
        }
    }
}
