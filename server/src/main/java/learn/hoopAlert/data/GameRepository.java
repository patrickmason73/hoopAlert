package learn.hoopAlert.data;

import learn.hoopAlert.models.Game;
import learn.hoopAlert.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByGameDate(LocalDate gameDate);
}
