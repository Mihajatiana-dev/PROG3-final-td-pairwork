package hei.school.prog3.dto.ClubRest;

import hei.school.prog3.dto.CoachRest.CoachResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class ClubResponse {
    private String id;
    private String name;
    private String acronym;
    private Integer yearCreation;
    private String stadium;
    private CoachResponse coach;
}