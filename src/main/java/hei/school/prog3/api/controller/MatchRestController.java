package hei.school.prog3.api.controller;

import hei.school.prog3.api.dto.request.AddGoal;
import hei.school.prog3.api.dto.request.UpdateMatchStatus;
import hei.school.prog3.model.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MatchRestController {

    @PostMapping("/matchMaker/{seasonYear}")
    public void createMatch() {

    }

    @GetMapping("/matches/{seasonYear}")
    public List<Match> getAllMatches() {
        return null;
    }

    @PutMapping("/matches/{id}/status")
    public Match updateMatchStatus(@PathVariable String id, @RequestBody UpdateMatchStatus status) {
        return null;
    }

    @PostMapping("/matches/{id}/goals")
    public Match addGoals(@PathVariable String id, @RequestBody List<AddGoal> goalsToAdd) {
        return null;
    }
}
