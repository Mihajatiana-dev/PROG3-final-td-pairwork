package hei.school.prog3.dto.Other;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class FilterCriteria {
    private String column;
    private Object value;
}
