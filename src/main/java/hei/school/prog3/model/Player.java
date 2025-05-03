package hei.school.prog3.model;

import hei.school.prog3.model.enums.PlayerPosition;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class Player {
    private String id;
    private String name;
    private Integer number;
    private PlayerPosition position;
    private String nationality;
    private Integer age;
    private Club club;

    public Player(String id, String name, Integer number, PlayerPosition position, String nationality, Integer age) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.position = position;
        this.nationality = nationality;
        this.age = age;
    }
}
