package hei.school.prog3.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CoachSimpleRequest {
    private String id;
    private String name;
    private String Nationality;
}
