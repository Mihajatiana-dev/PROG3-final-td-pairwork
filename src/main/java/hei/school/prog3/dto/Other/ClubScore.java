package hei.school.prog3.dto.Other;

import java.util.List;

import hei.school.prog3.dto.PlayerRest.Scorer;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClubScore {
    private int score;
    private List<Scorer> scorers;
}
