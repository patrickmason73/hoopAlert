package learn.hoopAlert.models;

import org.apache.tomcat.jni.Local;

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
    private LocalDateTime reminderTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false)
    private String opponent;

    @Column(nullable = false)
    private String gameTime;

    public Long getId() {
        return id;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public AppUser getUser() {
        return user;
    }

    public Team getTeam() {
        return team;
    }

    public String getOpponent() {
        return opponent;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }
}