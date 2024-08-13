package learn.hoopAlert.data;

import learn.hoopAlert.models.Reminder;
import learn.hoopAlert.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByUserAndReminderTimeBetween(AppUser user, LocalDateTime start, LocalDateTime end);
}
