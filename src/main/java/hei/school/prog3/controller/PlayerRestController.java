package hei.school.prog3.controller;

import hei.school.prog3.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlayerRestController {
    private final PlayerService playerService;

    @GetMapping("/players")
    public Object getAllPlayers() {
        return "not supported yet";
    }

    @PutMapping("/players")
    public Object saveAllPlayers() {
        return "not supported yet";
    }

    @GetMapping("/players/{id}/statistics/{seasonYear}")
    public Object getStatistics() {
        return "not supported yet";
    }
}
