package learn.hoopAlert.domain;

import learn.hoopAlert.data.ReminderRepository;
import learn.hoopAlert.models.Reminder;
import learn.hoopAlert.models.Schedule;
import learn.hoopAlert.models.SmsRequest;
import learn.hoopAlert.models.Team;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.data.ScheduleRepository;
import learn.hoopAlert.domain.TwilioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {

    private static final Logger logger = LoggerFactory.getLogger(ReminderService.class);

    private final ReminderRepository reminderRepository;
    private final ScheduleRepository scheduleRepository;
    private final TwilioService twilioService;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository, ScheduleRepository scheduleRepository, TwilioService twilioService) {
        this.reminderRepository = reminderRepository;
        this.scheduleRepository = scheduleRepository;
        this.twilioService = twilioService;
    }

    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    // Updated method to find reminders by LocalDateTime
    public List<Reminder> findRemindersByDate(LocalDateTime startOfDay) {
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1); // Calculate end of day
        return reminderRepository.findByReminderTimeBetween(startOfDay, endOfDay); // Find reminders between start and end of the day
    }

    // Method to create reminders for all upcoming games of a team for a specific user
    public void createRemindersForTeam(AppUser user, Team team) {
        List<Schedule> upcomingGames = scheduleRepository.findByHomeTeamOrAwayTeam(team, team);

        for (Schedule game : upcomingGames) {
            // Use reminderTime instead of gameDate to check for existing reminders
            LocalDateTime reminderTime = game.getGameDate().minusHours(1);

            // Skip if the game is in the past
            if (reminderTime.isBefore(LocalDateTime.now())) {
                continue;
            }

            boolean reminderExists = reminderRepository.existsByUserAndTeamAndReminderTime(user, team, reminderTime);

            if (!reminderExists) {
                Reminder reminder = new Reminder();
                reminder.setUser(user);
                reminder.setTeam(team);
                reminder.setReminderTime(reminderTime); // Reminder set 1 hour before game
                reminder.setOpponent(game.getAwayTeam().equals(team) ? game.getHomeTeam().getTeamName() : game.getAwayTeam().getTeamName());
                reminder.setGameTime(game.getGameDate().toLocalTime().toString());

                createReminder(reminder);
            }
        }
    }

    // Method to delete all reminders for a specific user and team
    public void deleteRemindersForTeam(AppUser user, Team team) {
        List<Reminder> reminders = reminderRepository.findByUserAndTeam(user, team);
        reminderRepository.deleteAll(reminders);
    }

    // Method to check and send reminders
    @Scheduled(cron = "0 0 8 * * ?")  // Example: Runs daily at 8 AM
    public void checkAndSendReminders() {
        List<Reminder> reminders = reminderRepository.findAll();
        for (Reminder reminder : reminders) {
            if (shouldSendReminder(reminder)) {
                String message = "Gameday! " + reminder.getTeam().getTeamName() + " is playing vs "
                        + reminder.getOpponent() + " at " + reminder.getGameTime() + " today!";
                sendReminder(reminder, message);
            }
        }
    }

    private boolean shouldSendReminder(Reminder reminder) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderDateTime = reminder.getReminderTime();
        LocalDateTime reminderWindowStart = reminderDateTime.minusHours(1);
        LocalDateTime reminderWindowEnd = reminderDateTime;
        return now.isAfter(reminderWindowStart) && now.isBefore(reminderWindowEnd);
    }

    public void sendReminder(Reminder reminder, String message) {
        try {
            SmsRequest smsRequest = new SmsRequest(reminder.getUser().getPhoneNumber(), message);
            twilioService.sendSms(smsRequest);
        } catch (Exception e) {
            logger.error("Failed to send SMS for reminder: " + reminder.getId(), e);
            // Handle the exception accordingly (e.g., retry mechanism, set a flag, etc.)
        }
    }
}
