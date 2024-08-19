package learn.hoopAlert.domain;

import learn.hoopAlert.data.ScheduleRepository;
import learn.hoopAlert.models.Schedule;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);


    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getScheduleByTeam(Long teamId) {
        return scheduleRepository.findByHomeTeamIdOrAwayTeamId(teamId, teamId);
    }

    public List<Schedule> getGamesOnDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        // Log the start and end of day
        logger.debug("Start of day: {}", startOfDay);
        logger.debug("End of day: {}", endOfDay);

        // Execute query
        List<Schedule> schedules = scheduleRepository.findByGameDateBetween(startOfDay, endOfDay);

        // Log the result
        logger.debug("Schedules found: {}", schedules);

        return schedules;
    }

    public void saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }
}