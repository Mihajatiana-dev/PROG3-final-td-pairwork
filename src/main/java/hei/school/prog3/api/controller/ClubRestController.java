package hei.school.prog3.api.controller;

import hei.school.prog3.api.RestMapper.ClubRestMapper;
import hei.school.prog3.api.dto.request.ClubSimpleRequest;
import hei.school.prog3.api.dto.response.ClubResponse;
import hei.school.prog3.dao.operations.ClubDAO;
import hei.school.prog3.model.Club;
import hei.school.prog3.model.Coach;
import hei.school.prog3.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @PutMapping("/clubs")
//    public ResponseEntity<Object> saveClubs(
//            @RequestBody List<ClubSimpleRequest> createClubs
//    ) {
//        if (createClubs == null || createClubs.isEmpty()) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        List<Club> clubsToSave = createClubs.stream()
//                .map(clubSimpleRequest -> {
//                            Club club = new Club(
//                                    clubSimpleRequest.getId(),
//                                    clubSimpleRequest.getName(),
//                                    clubSimpleRequest.getAcronym(),
//                                    clubSimpleRequest.getYearCreation(),
//                                    clubSimpleRequest.getStadium(),
//                                    new Coach(
//                                    null,
//                                        clubSimpleRequest.getCoach().getName(),
//                                        clubSimpleRequest.getCoach().getNationality()
//                                    )
//                            );
//                            return club;
//                        })
//                .toList();
//
//        List<Club> savedClubs = clubService.saveAllClubs(clubsToSave);
//        List<ClubResponse> clubResponses = savedClubs.stream()
//                .map(clubRestMapper::toRest)
//                .toList();
//
//        return ResponseEntity.ok(clubResponses);
//    }
}
