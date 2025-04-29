package hei.school.prog3.model;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class Scorer {
    private Player player;
    private Integer minuteOfGoal;
    private Boolean ownGoal;
}
