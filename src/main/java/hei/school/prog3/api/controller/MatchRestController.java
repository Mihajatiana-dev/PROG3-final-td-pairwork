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
    public void createMatch(@PathVariable int seasonYear) {

    }

    @GetMapping("/matches/{seasonYear}")
    public List<Match> getAllMatches(@PathVariable int seasonYear,
                                     @RequestParam(value = "matchStatus", required = false) String matchStatus,
                                     @RequestParam(value = "clubPlayingName", required = false) String clubPlayingName,
                                     @RequestParam(value = "matchAfter", required = false) String matchAfter,
                                     @RequestParam(value = "matchBeforeOrEquals", required = false) String matchBeforeOrEquals) {
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
