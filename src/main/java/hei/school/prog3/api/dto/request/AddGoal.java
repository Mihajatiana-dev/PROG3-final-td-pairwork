package hei.school.prog3.api.dto.request;

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
