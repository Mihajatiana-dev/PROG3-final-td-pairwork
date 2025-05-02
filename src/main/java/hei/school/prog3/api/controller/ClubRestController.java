package hei.school.prog3.api.controller;

import hei.school.prog3.api.RestMapper.ClubRestMapper;
import hei.school.prog3.api.dto.request.ClubSimpleRequest;
import hei.school.prog3.api.dto.response.ClubResponse;
import hei.school.prog3.api.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.dao.operations.ClubDAO;
import hei.school.prog3.model.Club;
import hei.school.prog3.model.Coach;
import hei.school.prog3.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ClubRestController {
    private final ClubService clubService;
    private final ClubRestMapper clubRestMapper;

    @GetMapping("/clubs")
    public ResponseEntity<Object> getAllPlayers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        List<Club> clubs = clubService.getAllClub(page, size);

        //JSON form
        List<ClubResponse> clubResponseList = clubs.stream()
                .map(clubRestMapper::toRest)
                .toList();
        return clubResponseList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clubResponseList);

    }

    @PutMapping("/clubs")
    public ResponseEntity<Object> saveClubs(
            @RequestBody List<ClubSimpleRequest> createClubs
    ) {
        List<Club> clubs = clubService.saveAllClubs(createClubs);
        if (createClubs == null || createClubs.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<ClubResponse> clubResponseList = clubs.stream()
                .map(clubRestMapper::toRest)
                .toList();
        return clubResponseList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clubResponseList);
    }

    @GetMapping("/clubs/{id}/players")
    public ResponseEntity<List<PlayerWithoutClub>> getPlayers(@PathVariable String id) {
        try {
            // Validate the UUID
            UUID.fromString(id);

            List<PlayerWithoutClub> players = clubService.getPlayers(id);

            return ResponseEntity.ok(players != null ? players : Collections.emptyList());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.emptyList());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @PutMapping("/clubs/{id}/players")
    public ResponseEntity<?> changePlayers(
            @RequestBody List<PlayerWithoutClub> playersToChange,
            @PathVariable String id) {

        try {
            if (playersToChange == null) {
                return ResponseEntity.badRequest().build();
            }

            List<PlayerWithoutClub> result = clubService.changePlayers(playersToChange, id);
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
