package hei.school.prog3.api.controller;

import hei.school.prog3.api.dto.request.AddGoal;
import hei.school.prog3.api.dto.request.UpdateMatchStatus;
import hei.school.prog3.model.FilterCriteria;
import hei.school.prog3.model.Match;
import hei.school.prog3.model.enums.MatchStatus;
import hei.school.prog3.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MatchRestController {
    private final MatchService matchService;

    @PostMapping("/matchMaker/{seasonYear}")
    public ResponseEntity<List<Match>> createMatch(@PathVariable int seasonYear) {
        List<Match> matches = matchService.createAll(seasonYear);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/matches/{seasonYear}")
    public ResponseEntity<List<Match>> getAllMatches(
            @PathVariable int seasonYear,
            @RequestParam(value = "matchStatus", required = false) String matchStatus,
            @RequestParam(value = "clubPlayingName", required = false) String clubPlayingName,
            @RequestParam(value = "matchAfter", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime matchAfter,
            @RequestParam(value = "matchBeforeOrEquals", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime matchBeforeOrEquals,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        List<FilterCriteria> filters = new ArrayList<>();

        if (matchStatus != null) {
            try {
                // Validate matchStatus is a valid enum value
                MatchStatus status = MatchStatus.valueOf(matchStatus);
                filters.add(new FilterCriteria("status", status.toString()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        if (clubPlayingName != null) {
            filters.add(new FilterCriteria("clubName", clubPlayingName));
        }
        if (matchAfter != null) {
            filters.add(new FilterCriteria("matchAfter", matchAfter));
        }
        if (matchBeforeOrEquals != null) {
            filters.add(new FilterCriteria("matchBeforeOrEquals", matchBeforeOrEquals));
        }

        List<Match> matches = matchService.getFilteredMatches(seasonYear, filters, page, size);

        return matches.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(matches);
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
