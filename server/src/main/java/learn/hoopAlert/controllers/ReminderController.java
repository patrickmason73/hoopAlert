package learn.hoopAlert.controllers;

import learn.hoopAlert.domain.ReminderService;
import learn.hoopAlert.models.Reminder;
import learn.hoopAlert.models.Team;
import learn.hoopAlert.domain.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;
    private final TeamService teamService;

    @Autowired
    public ReminderController(ReminderService reminderService, TeamService teamService) {
        this.reminderService = reminderService;
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<Reminder> createReminder(@RequestBody Reminder reminder) {
        Reminder createdReminder = reminderService.createReminder(reminder);
        return new ResponseEntity<>(createdReminder, HttpStatus.CREATED);
    }

    // Endpoint to manually trigger reminders for a specific date (for demo purposes)
    @PostMapping("/trigger/{date}")
    public ResponseEntity<String> triggerReminders(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {

        List<Reminder> reminders = reminderService.findRemindersByDate(date);

        if (reminders.isEmpty()) {
            return new ResponseEntity<>("No reminders found for the given date.", HttpStatus.NOT_FOUND);
        }

        for (Reminder reminder : reminders) {
            Team team = teamService.getTeamById(reminder.getTeam().getId()).orElse(null);
            if (team == null) {
                continue;
            }

            LocalTime gameTime = reminder.getReminderTime().toLocalTime();
            String message = "Gameday! " + team.getTeamName() + " is playing at " + gameTime + " today!";

            // Call the method in ReminderService to send the SMS
            reminderService.sendReminder(reminder, message);
        }

        return new ResponseEntity<>("Reminders triggered successfully for date: " + date.toString(), HttpStatus.OK);
    }

    // New endpoint to trigger reminders for the current day
    @PostMapping("/trigger/today")
    public ResponseEntity<String> triggerTodayReminders() {
        LocalDateTime today = LocalDate.now().atStartOfDay(); // Get the start of today as LocalDateTime
        System.out.println("Triggering today's reminders: " + today); // Debug log to confirm the method is invoked
        return triggerReminders(today);// Pass the start of the day as a LocalDateTime
    }
}