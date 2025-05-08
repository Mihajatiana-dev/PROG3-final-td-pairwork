package hei.school.prog3.dto.PlayerRest;

import hei.school.prog3.dto.ClubRest.ClubWithoutPlayerListResponse;
import hei.school.prog3.model.enums.PlayerPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class PlayerResponse {
    private String id;
    private String name;
    private Integer number;
    private PlayerPosition position;
    private String nationality;
    private Integer age;
    private ClubWithoutPlayerListResponse club;
}
