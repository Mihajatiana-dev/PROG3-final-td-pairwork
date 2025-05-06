package hei.school.prog3.model;

import hei.school.prog3.api.dto.response.ClubWithoutPlayerListResponse;

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
