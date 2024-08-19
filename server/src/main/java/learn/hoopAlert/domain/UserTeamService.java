package learn.hoopAlert.domain;

import learn.hoopAlert.data.AppUserRepository;
import learn.hoopAlert.data.TeamRepository;
import learn.hoopAlert.data.ScheduleRepository;
import learn.hoopAlert.data.ReminderRepository;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.models.Reminder;
import learn.hoopAlert.models.Team;
import learn.hoopAlert.models.Schedule;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserTeamService {

    private final AppUserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReminderRepository reminderRepository;

    public UserTeamService(AppUserRepository userRepository, TeamRepository teamRepository,
                           ScheduleRepository scheduleRepository, ReminderRepository reminderRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.scheduleRepository = scheduleRepository;
        this.reminderRepository = reminderRepository;
    }

    public Optional<AppUser> addTeamToUser(Long userId, Long teamId) {
        Optional<AppUser> userOpt = userRepository.findById(userId);
        Optional<Team> teamOpt = teamRepository.findById(teamId);

        if (userOpt.isPresent() && teamOpt.isPresent()) {
            AppUser user = userOpt.get();
            Team team = teamOpt.get();
            user.getTeams().add(team);

            // Generate reminders for each game in the schedule
            List<Schedule> teamSchedules = scheduleRepository.findByHomeTeamIdOrAwayTeamId(team.getId(), team.getId());

            for (Schedule schedule : teamSchedules) {
                Reminder reminder = new Reminder();
                reminder.setUser(user);
                reminder.setTeam(team);
                reminder.setGameTime(schedule.getGameDate().toString());
                reminder.setReminderTime(schedule.getGameDate()); // or adjust time based on requirement
                reminder.setOpponent(schedule.getHomeTeam().equals(team) ? schedule.getAwayTeam().getTeamName() : schedule.getHomeTeam().getTeamName());

                reminderRepository.save(reminder);
            }

            userRepository.save(user);
            return Optional.of(user);
        }

        return Optional.empty();
    }

    public Optional<AppUser> removeTeamFromUser(Long userId, Long teamId) {
        Optional<AppUser> userOpt = userRepository.findById(userId);
        Optional<Team> teamOpt = teamRepository.findById(teamId);

        if (userOpt.isPresent() && teamOpt.isPresent()) {
            AppUser user = userOpt.get();
            Team team = teamOpt.get();
            user.getTeams().remove(team);

            // Remove reminders associated with this team and user
            List<Reminder> reminders = reminderRepository.findByUserAndTeam(user, team);
            for (Reminder reminder : reminders) {
                reminderRepository.delete(reminder);
            }

            userRepository.save(user);
            return Optional.of(user);
        }

        return Optional.empty();
    }
}
