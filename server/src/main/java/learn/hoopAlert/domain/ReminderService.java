package learn.hoopAlert.domain;

import com.twilio.rest.chat.v1.service.User;
import learn.hoopAlert.data.AppUserRepository;
import learn.hoopAlert.data.ReminderRepository;
import learn.hoopAlert.data.TeamRepository;
import learn.hoopAlert.models.Game;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.models.Reminder;
import learn.hoopAlert.models.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final AppUserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TwilioService twilioService;

    public ReminderService(ReminderRepository reminderRepository, AppUserRepository userRepository, TeamRepository teamRepository, TwilioService twilioService) {
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.twilioService = twilioService;
    }
    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    // Method to check and send reminders
    @Scheduled(cron = "0 0 8 * * ?")  // Example: Runs daily at 8 AM
    public void checkAndSendReminders() {
        List<Reminder> reminders = reminderRepository.findAll();
        for (Reminder reminder : reminders) {
            // Check if the current time matches the reminder time
            if (shouldSendReminder(reminder)) {
                sendReminder(reminder);
            }
        }
    }

    private boolean shouldSendReminder(Reminder reminder) {
        LocalDateTime now = LocalDateTime.now(); // Current time
        LocalDateTime reminderDateTime = reminder.getReminderTime().toLocalDateTime(); // Reminder time

        // Check if the current time is within a certain window before the reminder time
        // ex: if we want to send reminders 1 hour before the game
        LocalDateTime reminderWindowStart = reminderDateTime.minusHours(1);
        LocalDateTime reminderWindowEnd = reminderDateTime;

        // Check if current time falls within the reminder window
        return now.isAfter(reminderWindowStart) && now.isBefore(reminderWindowEnd);
    }

    private void sendReminder(Reminder reminder) {
        String message = String.format("The %s has a game at %s tonight!",
                reminder.getTeam().getTeamName(), reminder.getReminderTime());
        twilioService.sendSms(new SmsRequest(reminder.getUser().getPhoneNumber(), message));
    }
}
