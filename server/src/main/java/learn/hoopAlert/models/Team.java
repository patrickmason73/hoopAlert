package learn.hoopAlert.models;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;
    private String teamCity;
    private String teamAbbreviation;

    @ManyToMany
    @JoinTable(name = "user_team",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<AppUser> users;

    // Getters and setters
}