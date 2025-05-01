package hei.school.prog3.model;

import hei.school.prog3.model.enums.DurationUnit;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PlayingTime {
    private Integer value;
    private DurationUnit durationUnit;
}