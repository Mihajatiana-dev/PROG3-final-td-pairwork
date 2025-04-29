package hei.school.prog3.model;

import hei.school.prog3.model.enums.ChampionshipName;
import hei.school.prog3.model.enums.Country;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Championship {
    private String id;
    private List<Club> clubs;
    private ChampionshipName championshipName;
    private Country country;
}
