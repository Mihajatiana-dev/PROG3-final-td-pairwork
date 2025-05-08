package hei.school.prog3.dto.Other;

import hei.school.prog3.model.enums.MatchStatus;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UpdateMatchStatus {
    private MatchStatus status;
}
