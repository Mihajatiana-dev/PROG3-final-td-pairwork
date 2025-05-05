package hei.school.prog3.model;

import lombok.*;

import java.util.List;

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
