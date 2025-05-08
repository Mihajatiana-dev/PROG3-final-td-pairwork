package hei.school.prog3.api.controller;

import hei.school.prog3.dto.ClubRest.*;
import hei.school.prog3.dto.RestMapper.ClubRestMapper;
import hei.school.prog3.dto.RestMapper.PlayerRestMapper;
import hei.school.prog3.dto.PlayerRest.PlayerWithoutClub;
import hei.school.prog3.exception.PlayerAlreadyAttachedException;
import hei.school.prog3.exception.PlayerInformationMismatchException;
import hei.school.prog3.exception.ResourceNotFoundException;
import hei.school.prog3.model.*;
import hei.school.prog3.service.ClubService;
import hei.school.prog3.service.TransfertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ClubRestController {
    private final ClubService clubService;
    private final ClubRestMapper clubRestMapper;
    private final PlayerRestMapper playerRestMapper;
    private final TransfertService transfertService;

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

            List<Player> playerModel = playersToChange.stream()
                    .map(playerRestMapper::toModel)
                    .collect(Collectors.toList());

            // Récupérer les joueurs actuels du club
            Club club = clubService.getClubWithPlayers(id);
            List<Player> currentPlayers = club.getPlayers();

            // Marquer les joueurs actuels comme "out"
            for (Player player : currentPlayers) {
                transfertService.saveTransfert(player.getId(), id, "out");
            }

            // Ajouter les nouveaux joueurs et les marquer comme "in"
            List<Player> result = clubService.changePlayers(playerModel, id);
            for (Player player : result) {
                transfertService.saveTransfert(player.getId(), id, "in");
            }

            List<PlayerWithoutClub> response = result.stream()
                    .map(playerRestMapper::toPlayerWithoutClub)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (PlayerAlreadyAttachedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PlayerInformationMismatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/clubs/{id}/players")
    public ResponseEntity<Object> addNewPlayerIntoCLub(
            @PathVariable String id,
            @RequestBody List<PlayerWithoutClub> players
    ) {
        try {
            UUID.fromString(id);

            if (players == null || players.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<Player> playerEntities = players.stream()
                    .map(playerRestMapper::toModel)
                    .collect(Collectors.toList());

            // Ajouter uniquement les nouveaux joueurs
            List<Player> addedPlayers = clubService.addPlayerIntoCLub(id, playerEntities);

            // Enregistrer les transferts uniquement pour les joueurs ajoutés
            for (Player player : playerEntities) {
                transfertService.saveTransfert(player.getId(), id, "in");
            }

            List<PlayerWithoutClub> result = addedPlayers.stream()
                    .map(playerRestMapper::toPlayerWithoutClub)
                    .collect(Collectors.toList());

            return result.isEmpty()
                    ? ResponseEntity.badRequest().build()
                    : ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (PlayerAlreadyAttachedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PlayerInformationMismatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/clubs/statistics/{seasonYear}")
    public ResponseEntity<List<ClubStatisticsRest>> getClubStatistics(@PathVariable int seasonYear, @RequestParam(defaultValue = "false") boolean hasToBeClassified) {
        List<ClubStatistics> statistics = clubService.getClubStatistics(seasonYear, hasToBeClassified);
        List<ClubStatisticsRest> statisticsRest = statistics.stream()
                .map(clubRestMapper::toClubStatisticsRest)
                .collect(Collectors.toList());
        return statisticsRest.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(statisticsRest);
    }

    @GetMapping("/championship/clubs")
    public ResponseEntity<List<ClubToFetch>> getAllChampionshipClubs() {
        List<ClubToFetch> clubs = clubService.getAllClubsWithStats();
        return ResponseEntity.ok(clubs);
    }

    @GetMapping("/championship/differenceGoalsMedian")
    public ResponseEntity<Integer> getDifferenceGoalsMedian() {
        int median = clubService.calculateDifferenceGoalsMedian();
        return ResponseEntity.ok(median);
    }
}