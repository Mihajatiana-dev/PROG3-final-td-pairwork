package hei.school.prog3.service;

import hei.school.prog3.dao.operations.PlayerDAO;
import hei.school.prog3.api.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.model.FilterCriteria;
import hei.school.prog3.model.Player;
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

    public List<Player> getAllFilteredPlayer(List<FilterCriteria> filterCriteriaList, int page, int size){
        return playerDAO.getAllFilteredPlayer(filterCriteriaList, page,size);
    }
}