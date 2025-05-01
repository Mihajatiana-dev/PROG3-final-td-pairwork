package hei.school.prog3.controller;

import hei.school.prog3.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlayerRestController {
    private final PlayerService playerService;

    @GetMapping("/players")
    public Object getAllPlayers() {
        return "not supported yet";
    }

    @PutMapping("/players")
    public ResponseEntity<List<PlayerWithoutClub>> createOrUpdatePlayers(@RequestBody List<PlayerWithoutClub> players) {
        List<PlayerWithoutClub> savedPlayers = playerService.saveAllPlayers(players);
        return savedPlayers.isEmpty() ? ResponseEntity.badRequest().build() : ResponseEntity.ok(savedPlayers);
    }

    @GetMapping("/players/{id}/statistics/{seasonYear}")
    public Object getStatistics() {
        return "not supported yet";
    }
}
