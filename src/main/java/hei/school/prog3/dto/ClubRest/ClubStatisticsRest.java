package hei.school.prog3.dto.ClubRest;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ClubStatisticsRest {
    private ClubWithoutPlayerListResponse club;
    private int rankingPoints;
    private int scoredGoals;
    private int concededGoals;
    private int differenceGoals;
    private int cleanSheetNumber;
}
