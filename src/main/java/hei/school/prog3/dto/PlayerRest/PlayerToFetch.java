package hei.school.prog3.dto.PlayerRest;

import hei.school.prog3.model.PlayingTime;
import hei.school.prog3.model.enums.PlayerPosition;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerToFetch {
    private String id;
    private String name;
    private Integer number;
    private PlayerPosition position;
    private String nationality;
    private Integer age;
    private Integer scoredGoals;
    private PlayingTime playingTime;
}
