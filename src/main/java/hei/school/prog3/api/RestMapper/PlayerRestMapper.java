package hei.school.prog3.api.RestMapper;

import hei.school.prog3.api.dto.response.ClubResponse;
import hei.school.prog3.api.dto.response.ClubWithoutPlayerListResponse;
import hei.school.prog3.api.dto.response.PlayerResponse;
import hei.school.prog3.model.Club;
import hei.school.prog3.api.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class PlayerRestMapper {
    @Autowired
    private CoachRestMapper coachRestMapper;

    public PlayerResponse toRest(Player player) {
        //create club response without player list
        ClubWithoutPlayerListResponse clubResponse = null;
        if (player.getClub() != null) {
            clubResponse = new ClubWithoutPlayerListResponse(
                    player.getClub().getId(),
                    player.getClub().getName(),
                    player.getClub().getAcronym(),
                    player.getClub().getYearCreation(),
                    player.getClub().getStadium(),
                    coachRestMapper.toRest(player.getClub().getCoach())
            );
        }
        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getNumber(),
                player.getPosition(),
                player.getNationality(),
                player.getAge(),
                clubResponse
        );
    }
    public Player toModel(PlayerWithoutClub playerWithoutClub) {
        Player player = new Player();
        player.setId(playerWithoutClub.getId());
        player.setName(playerWithoutClub.getName());
        player.setNumber(playerWithoutClub.getNumber());
        player.setPosition(playerWithoutClub.getPosition());
        player.setNationality(playerWithoutClub.getNationality());
        player.setAge(playerWithoutClub.getAge());
        return player;
    }

    public PlayerWithoutClub toPlayerWithoutClub(Player player) {
        PlayerWithoutClub dto = new PlayerWithoutClub();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setNumber(player.getNumber());
        dto.setPosition(player.getPosition());
        dto.setNationality(player.getNationality());
        dto.setAge(player.getAge());
        return dto;
    }
}