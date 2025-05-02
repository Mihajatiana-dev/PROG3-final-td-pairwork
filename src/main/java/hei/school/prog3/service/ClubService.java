package hei.school.prog3.service;

import hei.school.prog3.api.dto.request.ClubSimpleRequest;
import hei.school.prog3.dao.operations.ClubDAO;
import hei.school.prog3.model.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubDAO clubDAO;

    public List<Club> getAllClub(int page, int size) {
        return clubDAO.showAll(page, size);
    }

    public List<Club> saveAllClubs(List<ClubSimpleRequest> clubToSave) {
        return clubDAO.saveAll(clubToSave);
    }
}
