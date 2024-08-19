package learn.hoopAlert.data;

import learn.hoopAlert.models.Reminder;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByReminderTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Reminder> findByUserAndReminderTimeBetween(AppUser user, LocalDateTime start, LocalDateTime end);
    List<Reminder> findByReminderTime(LocalDateTime date);
    boolean existsByUserAndTeamAndReminderTime(AppUser user, Team team, LocalDateTime reminderTime);
    List<Reminder> findByUserAndTeam(AppUser user, Team team);
}
