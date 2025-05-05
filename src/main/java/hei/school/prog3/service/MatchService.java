package hei.school.prog3.service;

import hei.school.prog3.api.RestMapper.MatchConverter;
import hei.school.prog3.dao.operations.MatchDAO;
import hei.school.prog3.dao.operations.SeasonDAO;
import hei.school.prog3.model.FilterCriteria;
import hei.school.prog3.model.Match;
import hei.school.prog3.model.MatchWithAllInformations;
import hei.school.prog3.model.Season;
import hei.school.prog3.model.enums.SeasonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchDAO matchDAO;
    private final MatchConverter matchConverter;
    private final SeasonDAO seasonDAO;

//    public List<MatchWithAllInformations> generateSeasonMatches(String seasonId) {
//        try {
//            UUID seasonUUID = UUID.fromString(seasonId);
//            return matchDAO.createSeasonMatches(seasonUUID);
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("Invalid season ID format");
//        }
//    }             no useage, delete later

    public List<Match> createAll(int seasonYear) {
        // find season by year
        Season season = seasonDAO.findByYear(seasonYear)
                .orElseThrow(() -> new IllegalArgumentException("Seaoson not found for the year: " + seasonYear));

        // verify if season is not started
        if (season.getStatus() != SeasonStatus.NOT_STARTED) {
            throw new IllegalStateException("Season should have NOT_STARTED status");
        }

        // generate matches for season
        List<MatchWithAllInformations> matchesWithInfo = matchDAO.createSeasonMatches(UUID.fromString(season.getId()));

        // convert to Match
        return matchConverter.convertAll(matchesWithInfo);
    }

    public List<Match> getFilteredMatches(int seasonYear, List<FilterCriteria> filters, int page, int size) {
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("Page and size must be positive values");
        }

        // get filtered matches
        List<MatchWithAllInformations> matchesWithInfo = matchDAO.findBySeasonAndFilters(
                seasonYear,
                filters,
                page,
                size);

        // convert
        return matchConverter.convertAll(matchesWithInfo);
    }
}
