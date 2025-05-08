package hei.school.prog3.dto.MatchRest;

import hei.school.prog3.dto.Other.MatchClub;
import hei.school.prog3.model.enums.MatchStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class MatchMinimumInfo {
    private String id;
    private MatchClub clubPlayingHome;
    private MatchClub clubPlayingAway;
    private String stadium;
    private LocalDateTime matchDatetime;
    private MatchStatus actualStatus;
}
