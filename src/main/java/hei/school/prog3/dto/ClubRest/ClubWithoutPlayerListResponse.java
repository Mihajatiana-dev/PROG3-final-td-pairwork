package hei.school.prog3.dto.ClubRest;

import hei.school.prog3.dto.CoachRest.CoachResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClubWithoutPlayerListResponse {
    private String id;
    private String name;
    private String acronym;
    private Integer yearCreation;
    private String stadium;
    private CoachResponse coach;
}
