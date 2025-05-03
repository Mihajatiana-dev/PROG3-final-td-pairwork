package hei.school.prog3.api.RestMapper;

import hei.school.prog3.api.dto.response.ClubResponse;
import hei.school.prog3.api.dto.response.ClubWithoutPlayerListResponse;
import hei.school.prog3.api.dto.response.PlayerResponse;
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
        ClubWithoutPlayerListResponse clubResponse = new ClubWithoutPlayerListResponse(
                player.getClub().getId(),
                player.getClub().getName(),
                player.getClub().getAcronym(),
                player.getClub().getYearCreation(),
                player.getClub().getStadium(),
                coachRestMapper.apply(player.getClub().getCoach())
        );
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

    public static PlayerWithoutClub toPlayerWithoutClub(Player player) {
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