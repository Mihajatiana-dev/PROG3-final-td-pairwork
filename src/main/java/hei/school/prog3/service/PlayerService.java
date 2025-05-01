package hei.school.prog3.service;

import hei.school.prog3.dao.operations.PlayerDAO;
import hei.school.prog3.dto.rest.playerRest.PlayerWithoutClub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerDAO playerDAO;

    public List<PlayerWithoutClub> saveAllPlayers(List<PlayerWithoutClub> players) {
        return playerDAO.saveAll(players);
    }
}