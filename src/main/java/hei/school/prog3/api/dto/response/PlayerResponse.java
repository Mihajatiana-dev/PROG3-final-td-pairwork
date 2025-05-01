package hei.school.prog3.api.dto.response;

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
