package hei.school.prog3.api.controller;

import hei.school.prog3.api.dto.request.AddGoal;
import hei.school.prog3.api.dto.request.UpdateMatchStatus;
import hei.school.prog3.model.FilterCriteria;
import hei.school.prog3.model.MatchMinimumInfo;
import hei.school.prog3.model.enums.MatchStatus;
import hei.school.prog3.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MatchRestController {
    private final MatchService matchService;

    @PostMapping("/matchMaker/{seasonYear}")
    public ResponseEntity<List<MatchMinimumInfo>> createMatch(@PathVariable int seasonYear) {
        List<MatchMinimumInfo> matches = matchService.createAll(seasonYear);
        return ResponseEntity.ok(matches);  
    }

    @GetMapping("/matches/{seasonYear}")
    public ResponseEntity<List<MatchMinimumInfo>> getAllMatches(
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

        List<MatchMinimumInfo> matches = matchService.getFilteredMatches(seasonYear, filters, page, size);

        return matches.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(matches);
    }

    @PutMapping("/matches/{id}/status")
    public ResponseEntity<?> updateMatchStatus(@PathVariable String id, @RequestBody UpdateMatchStatus statusUpdate) {
        try {
            if (statusUpdate.getStatus() == null) {
                return ResponseEntity.badRequest().body("Error: Status cannot be null. Valid values are: NOT_STARTED, STARTED, FINISHED");
            }

            MatchMinimumInfo updatedMatch = matchService.updateMatchStatus(id, statusUpdate.getStatus());
            return ResponseEntity.ok(updatedMatch);
        } catch (ResponseStatusException e) {
            // Return a ResponseEntity with the error message and status code
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/matches/{id}/goals")
    public ResponseEntity<?> addGoals(@PathVariable String id, @RequestBody List<AddGoal> goalsToAdd) {
        try {
            MatchMinimumInfo updatedMatch = matchService.addGoals(id, goalsToAdd);
            return ResponseEntity.ok(updatedMatch);
        } catch (ResponseStatusException e) {
            // Return a ResponseEntity with the error message and status code
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
