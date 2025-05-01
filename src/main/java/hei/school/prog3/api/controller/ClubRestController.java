package hei.school.prog3.api.controller;

import hei.school.prog3.api.RestMapper.ClubRestMapper;
import hei.school.prog3.api.dto.response.ClubResponse;
import hei.school.prog3.dao.operations.ClubDAO;
import hei.school.prog3.model.Club;
import hei.school.prog3.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    ){
        List<Club> clubs = clubService.getAllClub(page, size);

        //JSON form
        List<ClubResponse> clubResponseList = clubs.stream()
                .map(clubRestMapper::toRest)
                .toList();
        return clubResponseList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clubResponseList);

    }
}
