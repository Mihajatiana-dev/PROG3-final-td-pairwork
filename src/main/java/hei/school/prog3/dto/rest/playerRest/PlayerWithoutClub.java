package hei.school.prog3.dto.rest.playerRest;

import hei.school.prog3.model.Club;
import hei.school.prog3.model.enums.PlayerPosition;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PlayerWithoutClub {
    private String id;
    private String name;
    private Integer number;
    private PlayerPosition position;
    private String nationality;
    private Integer age;
}
