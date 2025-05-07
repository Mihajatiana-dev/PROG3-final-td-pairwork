package hei.school.prog3.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Transfert {
    private String id;
    private String playerId;
    private String clubId;
    private String status;
    private LocalDateTime transfertDate;
}