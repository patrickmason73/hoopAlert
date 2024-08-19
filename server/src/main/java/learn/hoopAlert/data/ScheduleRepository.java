package learn.hoopAlert.data;

import learn.hoopAlert.models.Schedule;
import learn.hoopAlert.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // Existing methods
    List<Schedule> findByHomeTeamIdOrAwayTeamId(Long homeTeamId, Long awayTeamId);
    List<Schedule> findByGameDateBetween(LocalDateTime start, LocalDateTime end);

    // New method to find schedules by home or away team
    List<Schedule> findByHomeTeamOrAwayTeam(Team homeTeam, Team awayTeam);
}
