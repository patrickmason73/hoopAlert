package learn.hoopAlert.data;

import learn.hoopAlert.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByTeamAbbreviation(String abbreviation);
}