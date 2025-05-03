package hei.school.prog3.api.controller;

import hei.school.prog3.api.RestMapper.ClubRestMapper;
import hei.school.prog3.api.RestMapper.PlayerRestMapper;
import hei.school.prog3.api.dto.request.ClubSimpleRequest;
import hei.school.prog3.api.dto.response.ClubResponse;
import hei.school.prog3.api.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.model.Club;
import hei.school.prog3.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

            Club club = clubService.getClubWithPlayers(id);
            if (club == null) {
                return ResponseEntity.notFound().build();
            }

            List<PlayerWithoutClub> players = club.getPlayers().stream()
                    .map(PlayerRestMapper::toPlayerWithoutClub)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(players);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    @GetMapping("/clubs/statistics/{seasonYear}")
    public ResponseEntity<List<ClubResponse>> getClubStatistics(@PathVariable int seasonYear, @RequestParam(defaultValue = "false") boolean hasToBeClassified) {
        return null;
    }
}
