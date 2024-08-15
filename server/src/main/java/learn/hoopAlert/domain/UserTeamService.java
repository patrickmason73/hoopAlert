package learn.hoopAlert.domain;

import learn.hoopAlert.data.AppUserRepository;
import learn.hoopAlert.data.TeamRepository;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.models.Team;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTeamService {

    private final AppUserRepository userRepository;
    private final TeamRepository teamRepository;

    public UserTeamService(AppUserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public Optional<AppUser> addTeamToUser(Long userId, Long teamId) {
        Optional<AppUser> userOpt = userRepository.findById(userId);
        Optional<Team> teamOpt = teamRepository.findById(teamId);

        if (userOpt.isPresent() && teamOpt.isPresent()) {
            AppUser user = userOpt.get();
            Team team = teamOpt.get();
            user.getTeams().add(team);
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
            userRepository.save(user);
            return Optional.of(user);
        }

        return Optional.empty();
    }
}
