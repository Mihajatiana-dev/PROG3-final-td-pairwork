package hei.school.prog3.api.dto.request;


import hei.school.prog3.model.enums.SeasonStatus;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UpdateSeasonStatus {
    private SeasonStatus status;
}
