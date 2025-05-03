package hei.school.prog3.api.controller;

import hei.school.prog3.api.RestMapper.ClubRestMapper;
import hei.school.prog3.api.dto.request.ClubSimpleRequest;
import hei.school.prog3.api.dto.response.ClubResponse;
import hei.school.prog3.api.dto.rest.playerRest.PlayerWithoutClub;
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
    public ResponseEntity<List<ClubResponse>> getAllClubs(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        List<ClubResponse> clubResponseList = clubService.getAllClubResponses(page, size);
        return clubResponseList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(clubResponseList);
    }

    @PutMapping("/clubs")
    public ResponseEntity<List<ClubResponse>> saveClubs(
            @RequestBody List<ClubSimpleRequest> createClubs
    ) {
        if (createClubs == null || createClubs.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<ClubResponse> clubResponseList = clubService.saveAllClubResponses(createClubs);
        return clubResponseList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(clubResponseList);
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

    @PostMapping("/clubs/{id}/players")
    public ResponseEntity<Object> addNewPlayerIntoCLub(
            @PathVariable String id,
            @RequestBody List<PlayerWithoutClub> players
    ) {
            List<PlayerWithoutClub> result = clubService.addPlayerIntoCLub(id, players);
            return ResponseEntity.ok(result);
    }
}
