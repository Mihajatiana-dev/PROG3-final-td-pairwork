package hei.school.prog3.service;

import hei.school.prog3.api.RestMapper.ClubRestMapper;
import hei.school.prog3.api.RestMapper.PlayerRestMapper;
import hei.school.prog3.api.dto.request.ClubSimpleRequest;
import hei.school.prog3.api.dto.response.ClubResponse;
import hei.school.prog3.api.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.dao.operations.ClubDAO;
import hei.school.prog3.dao.operations.PlayerDAO;
import hei.school.prog3.exception.PlayerAlreadyAttachedException;
import hei.school.prog3.exception.ResourceNotFoundException;
import hei.school.prog3.model.Club;
import hei.school.prog3.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubDAO clubDAO;
    private final PlayerDAO playerDAO;
    private final ClubRestMapper clubRestMapper;

    public List<ClubResponse> getAllClubResponses(int page, int size) {
        return clubDAO.showAll(page, size)
                .stream()
                .map(clubRestMapper::toRest)
                .toList();
    }

    public List<ClubResponse> saveAllClubResponses(List<ClubSimpleRequest> clubToSave) {
        return clubDAO.saveAll(clubToSave)
                .stream()
                .map(clubRestMapper::toRest)
                .toList();
    }

//    public List<PlayerWithoutClub> getPlayers(String clubId) {
//        return clubDAO.getActualClubPlayers(clubId);
//    }

    public Club getClubWithPlayers(String clubId) {
        return clubDAO.getClubWithPlayers(clubId);
    }

    public List<PlayerWithoutClub> changePlayers(List<PlayerWithoutClub> playersToChange, String clubId) {
        return clubDAO.changePlayers(clubId, playersToChange);
    }

    public Club verifyExistingCLub(String Id){
        Club club = clubDAO.findById(Id);
        if (club == null || club.getId() == null) {
            throw new ResourceNotFoundException("Club with ID " + Id + " not found.");
        }
        return club;
    }

    public List<PlayerWithoutClub> addPlayerIntoCLub(String Id, List<PlayerWithoutClub> players) {
        //String Id == clubID
        Club club = verifyExistingCLub(Id);

        // Validate
        for (PlayerWithoutClub player : players) {
            Club existingClub = clubDAO.findClubByPlayerId(player.getId());

            if (existingClub != null && existingClub.getId() != null && !existingClub.getId().equals(club.getId())) {
                throw new PlayerAlreadyAttachedException("Player with ID " + player.getId() + " is already attached to another club.");
            }
        }

        // Insert
        for (PlayerWithoutClub player : players) {
            Player existingPlayer = playerDAO.findById(player.getId());
            if (existingPlayer == null) {
                Player newPlayer = new Player();
                newPlayer.setId(player.getId());
                newPlayer.setName(player.getName());
                newPlayer.setNumber(player.getNumber());
                newPlayer.setPosition(player.getPosition());
                newPlayer.setNationality(player.getNationality());
                newPlayer.setAge(player.getAge());
                newPlayer.setClub(club);

                playerDAO.savePLayerWithoutUpdate(List.of(newPlayer));
            } else {
                if (existingPlayer.getClub() == null || !existingPlayer.getClub().getId().equals(club.getId())) {
                    existingPlayer.setClub(club);
                    playerDAO.savePLayerWithoutUpdate(List.of(existingPlayer));
                }
            }
        }
        Club updatedClub = clubDAO.getClubWithPlayers(Id);
        return updatedClub.getPlayers().stream()
                .map(PlayerRestMapper::toPlayerWithoutClub)
                .toList();
    }
}

