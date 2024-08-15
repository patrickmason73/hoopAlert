package learn.hoopAlert.data;

import learn.hoopAlert.Security.SecurityConfig;
import learn.hoopAlert.models.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class AppUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AppUserRepository repository;

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setUsername("testUser");
        user.setPasswordHash("password");
        user.setPhoneNumber("+1234567890");
        user.setEmail("test@example.com");
        user.setEnabled(true);
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    void findByUsername_shouldReturnUser_whenUsernameExists() {
        Optional<AppUser> found = repository.findByUsername(user.getUsername());
        assertTrue(found.isPresent());
        assertEquals(user.getUsername(), found.get().getUsername());
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameDoesNotExist() {
        Optional<AppUser> found = repository.findByUsername("nonExistentUser");
        assertFalse(found.isPresent());
    }

    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        Optional<AppUser> found = repository.findByEmail(user.getEmail());
        assertTrue(found.isPresent());
        assertEquals(user.getEmail(), found.get().getEmail());
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        Optional<AppUser> found = repository.findByEmail("nonexistent@example.com");
        assertFalse(found.isPresent());
    }

    @Test
    void findByPhoneNumber_shouldReturnUser_whenPhoneNumberExists() {
        Optional<AppUser> found = repository.findByPhoneNumber(user.getPhoneNumber());
        assertTrue(found.isPresent());
        assertEquals(user.getPhoneNumber(), found.get().getPhoneNumber());
    }

    @Test
    void findByPhoneNumber_shouldReturnEmpty_whenPhoneNumberDoesNotExist() {
        Optional<AppUser> found = repository.findByPhoneNumber("+0987654321");
        assertFalse(found.isPresent());
    }

    @Test
    void updatePasswordByUsername_shouldUpdatePassword_whenUsernameExists() {
        String newPassword = "newPassword";
        repository.updatePasswordByUsername(newPassword, user.getUsername());
        entityManager.refresh(user);
        assertEquals(newPassword, user.getPasswordHash());
    }

    @Test
    void updatePasswordByUsername_shouldNotUpdatePassword_whenUsernameDoesNotExist() {
        String newPassword = "newPassword";
        repository.updatePasswordByUsername(newPassword, "nonExistentUser");
        entityManager.refresh(user);
        assertNotEquals(newPassword, user.getPasswordHash());
    }
}