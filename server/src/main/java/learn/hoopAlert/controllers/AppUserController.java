package learn.hoopAlert.controllers;

import learn.hoopAlert.Security.JwtService;
import learn.hoopAlert.domain.AppUserService;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.models.Team;
import learn.hoopAlert.domain.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;
    private final JwtService jwtConverter;

    @Autowired
    public AppUserController(AppUserService appUserService, JwtService jwtConverter) {
        this.appUserService = appUserService;
        this.jwtConverter = jwtConverter;
    }

    @GetMapping("/{userId}/teams")
    public ResponseEntity<Set<Team>> getTeamsForUser(@PathVariable Long userId) {
        Set<Team> teams = appUserService.getTeamsForUser(userId);
        return ResponseEntity.ok(teams);
    }

//    @PostMapping("/{userId}/teams/{teamId}")
//    public ResponseEntity<?> addTeamToUser(@PathVariable Long userId, @PathVariable Long teamId) {
//        Result<Void> result = appUserService.addTeamToUser(userId, teamId);
//        if (result.isSuccess()) {
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessages());
//        }
//    }

//    @DeleteMapping("/{userId}/teams/{teamId}")
//    public ResponseEntity<?> removeTeamFromUser(@PathVariable Long userId, @PathVariable Long teamId) {
//        Result<Void> result = appUserService.removeTeamFromUser(userId, teamId);
//        if (result.isSuccess()) {
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessages());
//        }
//    }

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

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Received Token: " + token);

            AppUser user = jwtConverter.getUserFromToken(token);

            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header is missing or malformed.");
        }
    }
}