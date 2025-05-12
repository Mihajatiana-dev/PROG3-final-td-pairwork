package hei.school.prog3.service;

import hei.school.prog3.dto.RestMapper.PlayerRestMapper;
import hei.school.prog3.dao.operations.PlayerDAO;
import hei.school.prog3.dto.Other.FilterCriteria;
import hei.school.prog3.model.Player;
import hei.school.prog3.dto.PlayerRest.PlayerStatistics;
import hei.school.prog3.dto.PlayerRest.PlayerToFetch;
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
    public List<Player> getAllFilteredPlayer(List<FilterCriteria> filterCriteriaList, int page, int size) {
        return playerDAO.getAllFilteredPlayer(filterCriteriaList, page, size);

    }

    public PlayerStatistics getPlayerStatistics(UUID playerId, int seasonYear) {
        PlayerStatistics statistics = new PlayerStatistics();
        statistics.setScoredGoals(playerDAO.getScoredGoals(playerId, seasonYear));
        statistics.setPlayingTime(playerDAO.getPlayingTime(playerId, seasonYear));
        return statistics;
    }

    public List<PlayerToFetch> getAllPlayersWithStats() {
        List<Player> players = playerDAO.findAllPlayers();
        return players.stream()
                .map(player -> {
                    PlayerToFetch playerToFetch = new PlayerToFetch();
                    playerToFetch.setId(player.getId());
                    playerToFetch.setName(player.getName());
                    playerToFetch.setNumber(player.getNumber());
                    playerToFetch.setPosition(player.getPosition());
                    playerToFetch.setNationality(player.getNationality());
                    playerToFetch.setAge(player.getAge());

                    // retrieve scored goals
                    playerToFetch.setScoredGoals(playerDAO.getScoredGoals(player.getId()));

                    // retrieve playing time
                    playerToFetch.setPlayingTime(playerDAO.getTotalPlayingTime(player.getId()));

                    return playerToFetch;
                })
                .toList();
    }
}