package hei.school.prog3.model;

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