package hei.school.prog3.dto.Other;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AddGoal {
    private String clubId;
    private String scorerIdentifier;
    private Integer minuteOfGoal;
}
