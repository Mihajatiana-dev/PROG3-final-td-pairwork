package hei.school.prog3.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Goal {
    private String id;
    private String playerId;
    private String clubId;
    private String matchId;
    private Integer minute;
    private Boolean ownGoal;
}