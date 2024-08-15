package learn.hoopAlert.controllers;

import learn.hoopAlert.domain.AppUserService;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.models.Team;
import learn.hoopAlert.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }


    @GetMapping("/{userId}/teams")
    public ResponseEntity<Set<Team>> getTeamsForUser(@PathVariable Long userId) {
        Set<Team> teams = appUserService.getTeamsForUser(userId);
        return ResponseEntity.ok(teams);
    }

    @PostMapping("/create")
    public Result<AppUser> createUser(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam String phoneNumber,
                                      @RequestParam String email) {
        return appUserService.create(username, password, phoneNumber, email);
    }

    @GetMapping("/user/{username}")
    public Optional<AppUser> getUserByUsername(@PathVariable String username) {
        return appUserService.findByUsername(username);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long userId, @RequestBody AppUser updatedUser) {
        AppUser user = appUserService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        appUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}