package hei.school.prog3.service;

import hei.school.prog3.api.dto.request.CreateSeason;
import hei.school.prog3.dao.operations.SeasonDAO;
import hei.school.prog3.model.Season;
import hei.school.prog3.model.enums.SeasonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final SeasonDAO seasonDAO;

    public List<Season> getAllSeasons() {
        return seasonDAO.getAllSeasons();
    }

    public List<Season> createSeasons(List<CreateSeason> seasonsToCreate){
        return seasonDAO.createSeasons(seasonsToCreate);
    }

    public Season updateSeasonStatus(int year, SeasonStatus newStatus){
        return seasonDAO.updateSeasonStatus(year, newStatus);
    }
}
