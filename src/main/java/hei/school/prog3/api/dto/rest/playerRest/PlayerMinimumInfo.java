package hei.school.prog3.api.dto.rest.playerRest;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PlayerMinimumInfo {
    private String id;
    private String name;
    private Integer number;
}
