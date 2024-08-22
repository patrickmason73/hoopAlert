package learn.hoopAlert.controllers;

import learn.hoopAlert.domain.ScheduleService;
import learn.hoopAlert.models.Schedule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/team/{teamId}")
    public List<Schedule> getScheduleByTeam(@PathVariable Long teamId) {
        return scheduleService.getScheduleByTeam(teamId);
    }

    @GetMapping("/date/{date}")
    public List<Schedule> getGamesOnDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return scheduleService.getGamesOnDate(localDate);
    }

    @PostMapping
    public void saveSchedule(@RequestBody Schedule schedule) {
        scheduleService.saveSchedule(schedule);
    }

    @GetMapping("/all")
    public List<Schedule> getAllScheduledGames() {
        return scheduleService.getAllScheduledGames();
    }


}