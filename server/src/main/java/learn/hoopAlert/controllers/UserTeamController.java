package learn.hoopAlert.controllers;

import learn.hoopAlert.domain.UserTeamService;
import learn.hoopAlert.models.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserTeamController {

    private final UserTeamService userTeamService;

    public UserTeamController(UserTeamService userTeamService) {
        this.userTeamService = userTeamService;
    }

    @PostMapping("/{userId}/teams/{teamId}")
    public ResponseEntity<AppUser> addTeamToUser(@PathVariable Long userId, @PathVariable Long teamId) {
        Optional<AppUser> user = userTeamService.addTeamToUser(userId, teamId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}/teams/{teamId}")
    public ResponseEntity<AppUser> removeTeamFromUser(@PathVariable Long userId, @PathVariable Long teamId) {
        Optional<AppUser> user = userTeamService.removeTeamFromUser(userId, teamId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}