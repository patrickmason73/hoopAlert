package learn.hoopAlert.data;

import learn.hoopAlert.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByNbaTeamId(String nbaTeamId);
    List<Team> findAll();
}