package learn.hoopAlert.domain;

import learn.hoopAlert.data.ReminderRepository;
import learn.hoopAlert.data.ScheduleRepository;
import learn.hoopAlert.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReminderServiceTest {

    @Mock
    private ReminderRepository reminderRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private TwilioService twilioService;

    @InjectMocks
    private ReminderService reminderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRemindersForTeam_createsReminders() {
        AppUser user = new AppUser();
        Team team = new Team();
        team.setId(1L);

        Schedule schedule = new Schedule();
        schedule.setGameDate(LocalDateTime.now().plusDays(1));
        schedule.setHomeTeam(team);
        schedule.setAwayTeam(new Team());

        when(scheduleRepository.findByHomeTeamOrAwayTeam(team, team))
                .thenReturn(List.of(schedule));
        when(reminderRepository.existsByUserAndTeamAndReminderTime(any(AppUser.class), any(Team.class), any(LocalDateTime.class)))
                .thenReturn(false);

        reminderService.createRemindersForTeam(user, team);

        verify(reminderRepository, times(1)).save(any(Reminder.class));
    }

    @Test
    void testCheckAndSendReminders_sendsReminders() {
        AppUser user = new AppUser();
        user.setPhoneNumber("+1234567890");
        Team team = new Team();
        team.setTeamName("Test Team");

        Reminder reminder = new Reminder();
        reminder.setUser(user);
        reminder.setTeam(team);
        reminder.setReminderTime(LocalDateTime.now().plusMinutes(30)); // Within the reminder window

        when(reminderRepository.findAll()).thenReturn(List.of(reminder));

        reminderService.checkAndSendReminders();

        verify(twilioService, times(1)).sendSms(any());
    }

    @Test
    void testFindRemindersByDate_returnsCorrectReminders() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        Reminder reminder = new Reminder();
        when(reminderRepository.findByReminderTimeBetween(startOfDay, endOfDay))
                .thenReturn(List.of(reminder));

        List<Reminder> reminders = reminderService.findRemindersByDate(startOfDay);

        assertEquals(1, reminders.size());
        verify(reminderRepository, times(1)).findByReminderTimeBetween(startOfDay, endOfDay);
    }

    @Test
    void testCheckAndSendReminders_doesNotSendOutsideWindow() {
        AppUser user = new AppUser();
        user.setPhoneNumber("+1234567890");
        Team team = new Team();
        team.setTeamName("Test Team");

        Reminder reminder = new Reminder();
        reminder.setUser(user);
        reminder.setTeam(team);
        reminder.setReminderTime(LocalDateTime.now().plusHours(2)); // Outside the reminder window

        when(reminderRepository.findAll()).thenReturn(List.of(reminder));

        reminderService.checkAndSendReminders();

        verify(twilioService, never()).sendSms(any());
    }

    @Test
    void testCreateRemindersForTeam_doesNotCreateDuplicateReminders() {
        AppUser user = new AppUser();
        Team team = new Team();
        team.setId(1L);

        Schedule schedule = new Schedule();
        schedule.setGameDate(LocalDateTime.now().plusDays(1));
        schedule.setHomeTeam(team);
        schedule.setAwayTeam(new Team());

        when(scheduleRepository.findByHomeTeamOrAwayTeam(team, team))
                .thenReturn(List.of(schedule));
        when(reminderRepository.existsByUserAndTeamAndReminderTime(any(AppUser.class), any(Team.class), any(LocalDateTime.class)))
                .thenReturn(true); // Simulate that the reminder already exists

        reminderService.createRemindersForTeam(user, team);

        verify(reminderRepository, never()).save(any(Reminder.class)); // Ensure no new reminder is saved
    }

    @Test
    void testCreateRemindersForTeam_handlesEmptySchedule() {
        AppUser user = new AppUser();
        Team team = new Team();
        team.setId(1L);

        when(scheduleRepository.findByHomeTeamOrAwayTeam(team, team))
                .thenReturn(Collections.emptyList());

        reminderService.createRemindersForTeam(user, team);

        verify(reminderRepository, never()).save(any(Reminder.class)); // Ensure no reminders are saved
    }

    @Test
    void testCreateRemindersForTeam_doesNotCreatePastReminders() {
        AppUser user = new AppUser();
        Team team = new Team();
        team.setId(1L);

        Schedule schedule = new Schedule();
        schedule.setGameDate(LocalDateTime.now().minusDays(1)); // Game in the past
        schedule.setHomeTeam(team);
        schedule.setAwayTeam(new Team());

        when(scheduleRepository.findByHomeTeamOrAwayTeam(team, team))
                .thenReturn(List.of(schedule));

        reminderService.createRemindersForTeam(user, team);

        verify(reminderRepository, never()).save(any(Reminder.class)); // Ensure no reminders are saved
    }

    @Test
    void testCheckAndSendReminders_handlesSmsFailure() {
        AppUser user = new AppUser();
        user.setPhoneNumber("+1234567890");
        Team team = new Team();
        team.setTeamName("Test Team");

        Reminder reminder = new Reminder();
        reminder.setUser(user);
        reminder.setTeam(team);
        reminder.setReminderTime(LocalDateTime.now().plusMinutes(30)); // Within the reminder window

        when(reminderRepository.findAll()).thenReturn(List.of(reminder));
        doThrow(new RuntimeException("SMS sending failed")).when(twilioService).sendSms(any(SmsRequest.class));

        reminderService.checkAndSendReminders();

        verify(twilioService, times(1)).sendSms(any(SmsRequest.class)); // Ensure we attempted to send the SMS
        // Further assertions could be made to check how the failure was handled, depending on your implementation
    }

    @Test
    void testFindRemindersByDate_handlesDateBoundaries() {
        LocalDateTime startOfDay = LocalDateTime.of(2024, 3, 10, 0, 0); // Example date around DST change
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        Reminder reminder = new Reminder();
        when(reminderRepository.findByReminderTimeBetween(startOfDay, endOfDay))
                .thenReturn(List.of(reminder));

        List<Reminder> reminders = reminderService.findRemindersByDate(startOfDay);

        assertEquals(1, reminders.size());
        verify(reminderRepository, times(1)).findByReminderTimeBetween(startOfDay, endOfDay);
    }

    @Test
    void testCreateRemindersForTeam_handlesTimezones() {
        AppUser user = new AppUser();
        user.setTimezone("America/New_York"); // Assuming you store the timezone

        Team team = new Team();
        team.setId(1L);

        Schedule schedule = new Schedule();
        schedule.setGameDate(LocalDateTime.now().plusDays(1));
        schedule.setHomeTeam(team);
        schedule.setAwayTeam(new Team());

        when(scheduleRepository.findByHomeTeamOrAwayTeam(team, team))
                .thenReturn(List.of(schedule));
        when(reminderRepository.existsByUserAndTeamAndReminderTime(any(AppUser.class), any(Team.class), any(LocalDateTime.class)))
                .thenReturn(false);

        reminderService.createRemindersForTeam(user, team);

        verify(reminderRepository, times(1)).save(any(Reminder.class));
    }
}