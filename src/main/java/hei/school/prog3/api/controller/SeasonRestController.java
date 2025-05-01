package hei.school.prog3.api.controller;

import hei.school.prog3.api.RestMapper.PlayerRestMapper;
import hei.school.prog3.api.dto.request.CreateSeason;
import hei.school.prog3.api.dto.request.UpdateSeasonStatus;
import hei.school.prog3.api.dto.response.PlayerResponse;
import hei.school.prog3.api.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.exception.BadRequestException;
import hei.school.prog3.exception.NotFoundException;
import hei.school.prog3.model.FilterCriteria;
import hei.school.prog3.model.Player;
import hei.school.prog3.model.PlayerStatistics;
import hei.school.prog3.model.Season;
import hei.school.prog3.model.enums.SeasonStatus;
import hei.school.prog3.service.PlayerService;
import hei.school.prog3.service.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SeasonRestController {
    private final SeasonService seasonService;

    @GetMapping("/seasons")
    public ResponseEntity<List<Season>> getAllSeasons() {
        List<Season> seasons = seasonService.getAllSeasons();
        return ResponseEntity.ok(seasons);
    }

    @PostMapping("/seasons")
    public ResponseEntity<List<Season>> createSeason(@RequestBody List<CreateSeason> seasons) {
        if (seasons == null || seasons.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Season> createdSeasons = seasonService.createSeasons(seasons);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeasons);
    }

    @PutMapping("/seasons/{seasonYear}/status")
    public ResponseEntity<?> updateSeasonStatus(
            @PathVariable int seasonYear,
            @RequestBody UpdateSeasonStatus request) {

        try {
            Season updated = seasonService.updateSeasonStatus(seasonYear, request.getStatus());
            return ResponseEntity.ok(updated);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur interne");
        }
    }
}
