package hei.school.prog3.dto.PlayerRest;

import hei.school.prog3.model.PlayingTime;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PlayerStatistics {
    private Integer scoredGoals;
    private PlayingTime playingTime;
}