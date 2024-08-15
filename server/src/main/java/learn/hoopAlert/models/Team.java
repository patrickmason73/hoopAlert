package learn.hoopAlert.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "team_city")
    private String teamCity;

    @Column(name = "team_abbreviation")
    private String teamAbbreviation;

    @Column(name = "nba_team_id", nullable = false, unique = true)
    private String nbaTeamId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(name = "user_team",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<AppUser> users;

    public Object getTeamName() {
        return teamName;
    }

    // Getters and setters
}