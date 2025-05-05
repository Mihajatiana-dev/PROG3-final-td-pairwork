package hei.school.prog3.service;

import hei.school.prog3.api.RestMapper.MatchConverter;
import hei.school.prog3.dao.operations.MatchDAO;
import hei.school.prog3.dao.operations.SeasonDAO;
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

    public List<MatchWithAllInformations> generateSeasonMatches(String seasonId) {
        try {
            UUID seasonUUID = UUID.fromString(seasonId);
            return matchDAO.createSeasonMatches(seasonUUID);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid season ID format");
        }
    }

    public List<Match> createAll(int seasonYear) {
        // 1. Trouver la saison par son année
        Season season = seasonDAO.findByYear(seasonYear)
                .orElseThrow(() -> new IllegalArgumentException("Saison non trouvée pour l'année: " + seasonYear));

        // 2. Vérifier que la saison est bien NOT_STARTED
        if (season.getStatus() != SeasonStatus.NOT_STARTED) {
            throw new IllegalStateException("La saison doit avoir le statut NOT_STARTED");
        }

        // 3. Générer les matchs
        List<MatchWithAllInformations> matchesWithInfo = matchDAO.createSeasonMatches(UUID.fromString(season.getId()));

        // 4. Convertir en Match
        return matchConverter.convertAll(matchesWithInfo);
    }
}
