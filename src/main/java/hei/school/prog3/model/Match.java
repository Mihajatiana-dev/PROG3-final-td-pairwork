package hei.school.prog3.model;

import hei.school.prog3.model.enums.MatchStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Match {
    private String id;
    private Club homeClub;
    private Club awayClub;
    private String stadium;
    private LocalDateTime matchDatetime;
    private MatchStatus actualStatus;
    private Season season;
}
