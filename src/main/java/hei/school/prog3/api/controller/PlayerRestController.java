package hei.school.prog3.api.controller;

import hei.school.prog3.api.RestMapper.PlayerRestMapper;
import hei.school.prog3.api.dto.response.PlayerResponse;
import hei.school.prog3.api.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.model.FilterCriteria;
import hei.school.prog3.model.Player;
import hei.school.prog3.model.PlayerStatistics;
import hei.school.prog3.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PlayerRestController {
    private final PlayerService playerService;
    private final PlayerRestMapper playerRestMapper;

    @GetMapping("/players")
    public ResponseEntity<Object> getAllPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "ageMinimum", required = false) Integer ageMinimum,
            @RequestParam(value = "ageMaximum", required = false) Integer ageMaximum,
            @RequestParam(value = "clubName", required = false) String clubName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        List<FilterCriteria> filterCriteriaList = new ArrayList<>();
        if (name != null) {
            filterCriteriaList.add(new FilterCriteria("name", name));
        }
        if (ageMinimum != null) {
            filterCriteriaList.add(new FilterCriteria("ageMinimum", ageMinimum));
        }
        if (ageMaximum != null) {
            filterCriteriaList.add(new FilterCriteria("ageMaximum", ageMaximum));
        }
        if (clubName != null) {
            filterCriteriaList.add(new FilterCriteria("clubName", clubName));
        }

        List<PlayerResponse> playersList = playerService.getAllFilteredPlayer(filterCriteriaList, page, size)
                .stream()
                .map(playerRestMapper::toRest)
                .toList();

        return playersList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(playersList);
    }

    @PutMapping("/players")
    public ResponseEntity<List<PlayerWithoutClub>> createOrUpdatePlayers(@RequestBody List<PlayerWithoutClub> players) {
        List<PlayerWithoutClub> savedPlayers = playerService.saveAllPlayers(players);
        return savedPlayers.isEmpty() ? ResponseEntity.badRequest().build() : ResponseEntity.ok(savedPlayers);
    }

    @GetMapping("/players/{id}/statistics/{seasonYear}")
    public ResponseEntity<PlayerStatistics> getPlayerStatistics(
            @PathVariable UUID id,
            @PathVariable int seasonYear) {

        PlayerStatistics stats = playerService.getPlayerStatistics(id, seasonYear);
        return ResponseEntity.ok(stats);
    }
}
