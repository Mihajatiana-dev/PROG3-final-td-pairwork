package hei.school.prog3.api.controller;

import hei.school.prog3.api.RestMapper.ClubRestMapper;
import hei.school.prog3.api.RestMapper.PlayerRestMapper;
import hei.school.prog3.api.dto.request.ClubSimpleRequest;
import hei.school.prog3.api.dto.response.ClubResponse;
import hei.school.prog3.api.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.model.Club;
import hei.school.prog3.model.Player;
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
    private final PlayerRestMapper playerRestMapper;

    @GetMapping("/clubs")
    public ResponseEntity<List<ClubResponse>> getAllClubs(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        List<ClubResponse> clubResponseList = clubService.getAllClubResponses(page, size)
                .stream()
                .map(clubRestMapper::toRest)
                .collect(Collectors.toList());

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
        List<Club> clubModel = createClubs.stream()
                .map(clubRestMapper::toModel)
                .collect(Collectors.toList());

        List<Club> requestClub = clubService.saveAllClubResponses(clubModel);

        List<ClubResponse> clubResponseList = requestClub.stream()
                .map(clubRestMapper::toRest)
                .collect(Collectors.toList());
        // Return the response
        return clubResponseList.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(clubResponseList);
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
                    .map(playerRestMapper::toPlayerWithoutClub)
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
            //map into model
            List<Player> playerModel = playersToChange.stream()
                    .map(playerRestMapper::toModel)
                    .collect(Collectors.toList());
            List<Player> result = clubService.changePlayers(playerModel, id);
            //map into dto for return
            List<PlayerWithoutClub> response = result.stream()
                    .map(playerRestMapper::toPlayerWithoutClub)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/clubs/{id}/players")
    public ResponseEntity<Object> addNewPlayerIntoCLub(
            @PathVariable String id,
            @RequestBody List<PlayerWithoutClub> players
    ) {
            List<Player> playerEntities = players.stream()
                .map(playerRestMapper::toModel)
                .collect(Collectors.toList());
            //Add players into club
            List<Player> updatedPlayers = clubService.addPlayerIntoCLub(id, playerEntities);
            //return result for api
            List<PlayerWithoutClub> result = updatedPlayers.stream()
                .map(playerRestMapper::toPlayerWithoutClub)
                .collect(Collectors.toList());

            return result.isEmpty()
                    ? ResponseEntity.badRequest().build()
                    : ResponseEntity.ok(result);
    }

    @GetMapping("/clubs/statistics/{seasonYear}")
    public ResponseEntity<List<ClubResponse>> getClubStatistics(@PathVariable int seasonYear, @RequestParam(defaultValue = "false") boolean hasToBeClassified) {
        return null;
    }
}
