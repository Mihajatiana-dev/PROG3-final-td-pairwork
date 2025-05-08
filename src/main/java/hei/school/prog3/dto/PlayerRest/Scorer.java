package hei.school.prog3.dto.PlayerRest;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Scorer {
    private PlayerMinimumInfo player;
    private Integer minuteOfGoal;
    private Boolean ownGoal;
}
