package learn.hoopAlert.domain;

import learn.hoopAlert.App;
import learn.hoopAlert.Security.SecurityConfig;
import learn.hoopAlert.TwilioConfig;
import learn.hoopAlert.data.AppUserRepository;
import learn.hoopAlert.data.RoleRepository;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = App.class)
@TestPropertySource("classpath:application-test.properties")
class AppUserServiceTest {

    @MockBean
    AppUserRepository repository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder encoder;

    @Autowired
    AppUserService appUserService;

    @MockBean
    TwilioService twilioService;


    @Test
    void create_shouldReturnSuccess_whenValidData() {
        String username = "validUser";
        String password = "ValidPass1!";
        String phoneNumber = "+1234567890";
        String email = "valid@example.com";

        when(repository.findByUsername(username)).thenReturn(Optional.empty());
        when(repository.findByEmail(email)).thenReturn(Optional.empty());
        when(repository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(new Role("USER"));

        AppUser savedUser = new AppUser();
        savedUser.setUsername(username);
        savedUser.setPasswordHash("encodedPassword");
        savedUser.setPhoneNumber(phoneNumber);
        savedUser.setEmail(email);
        savedUser.setEnabled(true);
        savedUser.setRoles(List.of(new Role("USER")));

        when(repository.save(any(AppUser.class))).thenReturn(savedUser);

        // Call the create method
        Result<AppUser> result = appUserService.create(username, password, phoneNumber, email);

        // Validate the result
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(username, result.getPayload().getUsername());
        verify(repository, times(1)).save(any(AppUser.class));
    }

    @Test
    void create_shouldReturnFailure_whenUsernameExists() {
        String username = "newUser2";
        String password = "ValidPass1!";
        String phoneNumber = "+1234567890";
        String email = "valid@example.com";

        AppUser existingUser = new AppUser();
        existingUser.setUsername(username);
        when(repository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        // Call the real service method to allow it to perform validation logic.
        Result<AppUser> result = appUserService.create(username, password, phoneNumber, email);
        System.out.println("Result Success: " + result.isSuccess());
        System.out.println("Result Messages: " + result.getMessages());

        // Assert the result
        assertFalse(result.isSuccess());
        assertEquals("Username already exists", result.getMessages().get(0));

        // Verify repository save is not called
        verify(repository, never()).save(any(AppUser.class));
    }

    @Test
    void create_shouldReturnFailure_whenEmailExists() {
        String username = "newUser2";
        String password = "ValidPass1!";
        String phoneNumber = "+1234567890";
        String email = "existing@example.com";

        AppUser existingUser = new AppUser();
        existingUser.setEmail(email);

        when(repository.findByEmail(email)).thenReturn(Optional.of(new AppUser()));

        Result<AppUser> result = appUserService.create(username, password, phoneNumber, email);

        assertFalse(result.isSuccess());
        assertEquals("Email already exists", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
    }

    @Test
    void create_shouldReturnFailure_whenPhoneNumberExists() {
        String username = "validUser";
        String password = "ValidPass1!";
        String phoneNumber = "+1234567890";
        String email = "valid@example.com";

        when(repository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(new AppUser()));

        Result<AppUser> result = appUserService.create(username, password, phoneNumber, email);

        assertFalse(result.isSuccess());
        assertEquals("Phone number already exists", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
    }

    @Test
    void create_shouldReturnFailure_whenInvalidPassword() {
        String username = "validUser";
        String password = "short";
        String phoneNumber = "+1234567890";
        String email = "valid@example.com";

        Result<AppUser> result = appUserService.create(username, password, phoneNumber, email);

        assertFalse(result.isSuccess());
        assertEquals("Password must be at least 8 characters and contain a digit, a letter, and a non-digit/non-letter", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
    }

    @Test
    void create_shouldReturnFailure_whenInvalidEmail() {
        String username = "validUser";
        String password = "ValidPass1!";
        String phoneNumber = "+1234567890";
        String email = "invalidEmail";

        Result<AppUser> result = appUserService.create(username, password, phoneNumber, email);

        assertFalse(result.isSuccess());
        assertEquals("Invalid email format", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
    }

    @Test
    void create_shouldReturnFailure_whenInvalidPhoneNumber() {
        String username = "validUser";
        String password = "ValidPass1!";
        String phoneNumber = "invalidPhone";
        String email = "valid@example.com";

        Result<AppUser> result = appUserService.create(username, password, phoneNumber, email);

        assertFalse(result.isSuccess());
        assertEquals("Invalid phone number format", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
    }
}