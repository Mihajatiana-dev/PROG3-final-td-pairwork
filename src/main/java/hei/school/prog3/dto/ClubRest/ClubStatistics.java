package hei.school.prog3.dto.ClubRest;

import hei.school.prog3.model.Club;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClubStatistics {
    private Club club;
    private int rankingPoints;
    private int scoredGoals;
    private int concededGoals;
    private int differenceGoals;
    private int cleanSheetNumber;
}