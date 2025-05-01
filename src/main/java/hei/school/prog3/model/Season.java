package hei.school.prog3.model;

import hei.school.prog3.model.enums.SeasonStatus;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class Season {
    private String id;
    private Integer year;
    private String alias;
    private SeasonStatus status;
}
