package learn.hoopAlert.models;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String team;
    private LocalDate gameDate;

    public String getTeam() {
        return team;
    }

    // Getters and Setters
}
