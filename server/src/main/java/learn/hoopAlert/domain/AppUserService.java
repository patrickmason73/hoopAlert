package learn.hoopAlert.domain;

import learn.hoopAlert.data.AppUserRepository;
import learn.hoopAlert.data.RoleRepository;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Set;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private final AppUserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepository repository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public AppUser findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return appUser;
    }

    public Result<AppUser> create(String username, String password, String phoneNumber, String email) {
        Result<AppUser> result = validate(username, password, phoneNumber, email);
        if (!result.isSuccess()) {
            return result;
        }

        password = encoder.encode(password);

        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPasswordHash(password); // Ensure this is correct
        appUser.setEnabled(true);
        appUser.setPhoneNumber(phoneNumber);
        appUser.setEmail(email);

        // Fetch the ROLE_USER role
        Role userRole = roleRepository.findByName("USER"); // Make sure 'USER' role exists
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
//            result.addMessage(ActionStatus.INVALID, "The provided username already exists");
        }

        return result;
    }

    private Result<AppUser> validate(String username, String password, String email, String phoneNumber) {
        Result<AppUser> result = new Result<>();

        // Validate username
        if (username == null || username.isBlank()) {
            result.addMessage(ActionStatus.INVALID, "Username is required");
            return result;
        }

        if (username.length() > 50) {
            result.addMessage(ActionStatus.INVALID, "Username must be less than 50 characters");
        }

        // Validate password
        if (password == null) {
            result.addMessage(ActionStatus.INVALID, "Password is required");
            return result;
        }

        if (!isValidPassword(password)) {
            result.addMessage(ActionStatus.INVALID,
                    "Password must be at least 8 characters and contain a digit, " +
                            "a letter, and a non-digit/non-letter");
        }

        // Validate email
        if (email == null || email.isBlank()) {
            result.addMessage(ActionStatus.INVALID, "Email is required");
            return result;
        }

        if (!isValidEmail(email)) {
            result.addMessage(ActionStatus.INVALID, "Invalid email format");
        }

        // Validate phone number
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
        if (password.length() < 8) {
            return false;
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        return digits > 0 && letters > 0 && others > 0;
    }

    private boolean isValidEmail(String email) {
        // Basic email pattern check; consider using a more robust validation if needed
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Basic phone number validation: checks for digits and length
        return phoneNumber.matches("^\\d{1,20}$");
    }
}