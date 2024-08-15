package learn.hoopAlert.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private Timestamp reminderTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    public Team getTeam() {
        return team;
    }

    public Timestamp getReminderTime() {
        return reminderTime;
    }

    public AppUser getUser() {
        return user;
    }

    // Getters and setters
}
