package learn.hoopAlert.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/{userId}/teams")
    public ResponseEntity<?> addPreferredTeam(@PathVariable Long userId, @RequestBody Team team) {
        userService.addPreferredTeam(userId, team);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}/teams")
    public ResponseEntity<List<Team>> getPreferredTeams(@PathVariable Long userId) {
        List<Team> teams = userService.getPreferredTeams(userId);
        return ResponseEntity.ok(teams);
    }
}
