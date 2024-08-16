package learn.hoopAlert.domain;

import learn.hoopAlert.models.Team;
import learn.hoopAlert.data.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;



    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    public Optional<Team> getTeamByNbaTeamId(String nbaTeamId) {
        return teamRepository.findByNbaTeamId(nbaTeamId);
    }

}
