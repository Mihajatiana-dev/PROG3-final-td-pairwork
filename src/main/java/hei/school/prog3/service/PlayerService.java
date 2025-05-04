package hei.school.prog3.service;

import hei.school.prog3.api.RestMapper.PlayerRestMapper;
import hei.school.prog3.api.dto.response.PlayerResponse;
import hei.school.prog3.dao.operations.PlayerDAO;
import hei.school.prog3.api.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.model.FilterCriteria;
import hei.school.prog3.model.Player;
import hei.school.prog3.model.PlayerStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerDAO playerDAO;
    private final PlayerRestMapper playerRestMapper;

        public List<Player> saveAllPlayers(List<Player> players) {
        return playerDAO.saveAll(players);
    }
    //map into JSON
    public List<Player> getAllFilteredPlayer(List<FilterCriteria> filterCriteriaList, int page, int size){
        return playerDAO.getAllFilteredPlayer(filterCriteriaList, page,size);

    }

    public PlayerStatistics getPlayerStatistics(UUID playerId, int seasonYear) {
        return playerDAO.getPlayerStatistics(playerId, seasonYear);
    }
}