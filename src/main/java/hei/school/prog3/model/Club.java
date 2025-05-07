package hei.school.prog3.model;

import lombok.*;

import java.util.Collections;
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

    public Club(String id, String name, String acronym, Integer yearCreation, String stadium, Coach coach) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.yearCreation = yearCreation;
        this.stadium = stadium;
        this.coach = coach;
    }

    public Club(String id, String name, String acronym, Integer yearCreation, String stadium) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.yearCreation = yearCreation;
        this.stadium = stadium;
    }

    public List<Player> getPlayers() {
        return this.playerList != null ? this.playerList : Collections.emptyList();
    }

    public Club(String id) {
        this.id = id;
    }
}
