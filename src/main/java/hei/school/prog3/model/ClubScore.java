package hei.school.prog3.model;

import java.util.List;

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
