package hei.school.prog3.model;

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
