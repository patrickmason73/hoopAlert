package learn.hoopAlert.domain;

import learn.hoopAlert.Security.JwtService;
import learn.hoopAlert.data.AppUserRepository;
import learn.hoopAlert.data.RoleRepository;
import learn.hoopAlert.data.TeamRepository;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.models.Reminder;
import learn.hoopAlert.models.Role;
import learn.hoopAlert.models.Schedule;
import learn.hoopAlert.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final TeamRepository teamRepository;
    private final ReminderService reminderService;
    private final JwtService jwtService;  // Inject JwtService

    @Autowired
    public AppUserService(AppUserRepository repository,
                          RoleRepository roleRepository,
                          PasswordEncoder encoder,
                          TeamRepository teamRepository,
                          ReminderService reminderService,
                          @Lazy JwtService jwtService) {  // Add JwtService to constructor
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.teamRepository = teamRepository;
        this.reminderService = reminderService;
        this.jwtService = jwtService;  // Assign JwtService
    }


    public Set<Team> getTeamsForUser(Long userId) {
        Optional<AppUser> user = repository.findById(userId);
        return user.map(AppUser::getTeams).orElse(Set.of());
    }

    public Optional<Team> findTeamById(Long teamId) {
        return teamRepository.findById(teamId);
    }

    public Result<Void> addTeamToUser(Long userId, Long teamId) {
        Result<Void> result = new Result<>();

        Optional<AppUser> userOpt = repository.findById(userId);
        if (userOpt.isEmpty()) {
            result.addMessage(ActionStatus.INVALID, "User not found");
            return result;
        }

        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (teamOpt.isEmpty()) {
            result.addMessage(ActionStatus.INVALID, "Team not found");
            return result;
        }

        AppUser user = userOpt.get();
        Team team = teamOpt.get();

        if (user.getTeams().contains(team)) {
            result.addMessage(ActionStatus.INVALID, "Team already added to the user");
            return result;
        }

        // Add the team to the user's list of teams
        user.getTeams().add(team);
        repository.save(user);

        // Create reminders for this user and team
        reminderService.createRemindersForTeam(user, team);

        return result;
    }

    public Result<Void> removeTeamFromUser(Long userId, Long teamId) {
        Result<Void> result = new Result<>();

        Optional<AppUser> userOpt = repository.findById(userId);
        if (userOpt.isEmpty()) {
            result.addMessage(ActionStatus.INVALID, "User not found");
            return result;
        }

        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (teamOpt.isEmpty()) {
            result.addMessage(ActionStatus.INVALID, "Team not found");
            return result;
        }

        AppUser user = userOpt.get();
        Team team = teamOpt.get();

        // Remove the team from the user's list of teams
        user.getTeams().remove(team);
        repository.save(user);

        // Delete reminders for this user and team
        reminderService.deleteRemindersForTeam(user, team);

        return result;
    }

    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = repository.findByUsername(username);

        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        AppUser user = appUser.get();
        System.out.println("Loaded user: " + user.getUsername() + ", Password Hash: " + user.getPasswordHash());

        return user;
    }

    public Optional<AppUser> findById(Long userId) {
        return repository.findById(userId);
    }

    public Optional<AppUser> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Optional<AppUser> findByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    public Optional<AppUser> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Result<AppUser> create(String username, String password, String phoneNumber, String email) {
        Result<AppUser> result = validate(username, password, phoneNumber, email);
        if (!result.isSuccess()) {
            return result;
        }

        password = encoder.encode(password);

        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPasswordHash(password);
        appUser.setEnabled(true);
        appUser.setPhoneNumber(phoneNumber);
        appUser.setEmail(email);

        // Fetch the ROLE_USER role
        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role("USER");
            roleRepository.save(userRole);
        }
        appUser.setRoles(List.of(userRole));

        try {
            appUser = repository.save(appUser);
            result.setPayload(appUser);
        } catch (DataIntegrityViolationException e) {
            System.err.println("DataIntegrityViolationException: " + e.getMessage());
        }

        return result;
    }

    public AppUser updateUser(Long userId, AppUser updatedUser) {
        return repository.findById(userId)
                .map(user -> {
                    boolean usernameChanged = false;

                    if (updatedUser.getUsername() != null) {
                        user.setUsername(updatedUser.getUsername());
                        usernameChanged = true;
                    }
                    if (updatedUser.getPhoneNumber() != null) {
                        user.setPhoneNumber(updatedUser.getPhoneNumber());
                    }
                    if (updatedUser.getEmail() != null) {
                        user.setEmail(updatedUser.getEmail());
                    }
                    if (updatedUser.getPasswordHash() != null && !updatedUser.getPasswordHash().equals(user.getPasswordHash())) {
                        // Ensure that we only encode the password if it's not already encoded
                        if (!encoder.matches(updatedUser.getPasswordHash(), user.getPasswordHash())) {
                            user.setPasswordHash(encoder.encode(updatedUser.getPasswordHash()));
                        }
                    }
                    AppUser savedUser = repository.save(user);

                    if (usernameChanged) {
                        String newJwt = jwtService.getTokenFromUser(savedUser);
                        // Handle the new JWT as needed
                    }

                    return savedUser;
                }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long userId) {
        if (repository.existsById(userId)) {
            repository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private Result<AppUser> validate(String username, String password, String phoneNumber, String email) {
        Result<AppUser> result = new Result<>();

        if (repository.findByUsername(username).isPresent()) {
            result.addMessage(ActionStatus.INVALID, "Username already exists");
            return result;
        }

        if (repository.findByEmail(email).isPresent()) {
            result.addMessage(ActionStatus.INVALID, "Email already exists");
            return result;
        }

        if (repository.findByPhoneNumber(phoneNumber).isPresent()) {
            result.addMessage(ActionStatus.INVALID, "Phone number already exists");
            return result;
        }

        if (username == null || username.isBlank()) {
            result.addMessage(ActionStatus.INVALID, "Username is required");
            return result;
        }

        if (username.length() > 50) {
            result.addMessage(ActionStatus.INVALID, "Username must be less than 50 characters");
        }

        if (password == null) {
            result.addMessage(ActionStatus.INVALID, "Password is required");
            return result;
        }

        if (!isValidPassword(password)) {
            result.addMessage(ActionStatus.INVALID,
                    "Password must be at least 8 characters and contain a digit, " +
                            "a letter, and a non-digit/non-letter");
        }

        if (email == null || email.isBlank()) {
            result.addMessage(ActionStatus.INVALID, "Email is required");
            return result;
        }

        if (!isValidEmail(email)) {
            result.addMessage(ActionStatus.INVALID, "Invalid email format");
        }

        if (phoneNumber == null || phoneNumber.isBlank()) {
            result.addMessage(ActionStatus.INVALID, "Phone number is required");
            return result;
        }

        if (phoneNumber.length() > 20) {
            result.addMessage(ActionStatus.INVALID, "Phone number must be less than 20 characters");
            return result;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            result.addMessage(ActionStatus.INVALID, "Invalid phone number format");
        }

        return result;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+?[0-9\\-\\s()]{7,20}$");
    }
}
