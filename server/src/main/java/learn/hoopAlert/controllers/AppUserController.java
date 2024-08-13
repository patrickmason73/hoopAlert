package learn.hoopAlert.controllers;

import learn.hoopAlert.domain.AppUserService;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/create")
    public Result<AppUser> createUser(@RequestParam String username, @RequestParam String password) {
        return appUserService.create(username, password);
    }

    @GetMapping("/user/{username}")
    public AppUser getUserByUsername(@PathVariable String username) {
        return (AppUser) appUserService.loadUserByUsername(username);
    }
}