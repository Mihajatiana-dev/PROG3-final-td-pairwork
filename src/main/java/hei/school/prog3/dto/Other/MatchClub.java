package hei.school.prog3.dto.Other;

import hei.school.prog3.dto.ClubRest.ClubMinimumInfo;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MatchClub {
    private ClubMinimumInfo club;
    private ClubScore clubScore;
}
