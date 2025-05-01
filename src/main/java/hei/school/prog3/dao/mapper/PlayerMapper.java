package hei.school.prog3.dao.mapper;

import hei.school.prog3.dao.operations.ClubDAO;
import hei.school.prog3.model.Club;
import hei.school.prog3.model.Player;
import hei.school.prog3.model.enums.PlayerPosition;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor

public class PlayerMapper implements Function<ResultSet, Player> {
    private final ClubDAO clubDAO;

    @Override
    @SneakyThrows
    public Player apply(ResultSet resultSet) {
        String playerId = resultSet.getString("player_id");
        Club club = clubDAO.findClubByPlayerId(playerId);

        Player player = new Player();
        player.setId(playerId);
        player.setName(resultSet.getString("player_name"));
        player.setNumber(resultSet.getInt("number"));
        player.setPosition(PlayerPosition.valueOf(resultSet.getString("position")));
        player.setNationality(resultSet.getString("nationality"));
        player.setAge(resultSet.getInt("age"));
        player.setClub(club);

        return player;
    }
}
