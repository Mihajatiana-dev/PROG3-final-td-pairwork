package hei.school.prog3.dto.SeasonRest;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CreateSeason {
    private Integer year;
    private String alias;
}
