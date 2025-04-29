package hei.school.prog3.model;

import lombok.*;

import java.util.List;
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Club {
    private String id;
    private String name;
    private String acronym;
    private Integer yearCreation;
    private String stadium;
    private List<Player> playerList;
    private Coach coach;
}
