package hei.school.prog3.dao.operations;

import hei.school.prog3.config.DbConnection;
import hei.school.prog3.model.PlayingTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PlayingTimeDAO {
    private final DbConnection dataSource;

    public PlayingTime fetchPlayingTimeByPlayerId(UUID playerId) {
        return null;
    }
}
