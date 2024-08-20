package learn.hoopAlert.data;

import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.models.Role;
import learn.hoopAlert.models.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setUsername("testuser");
        user.setPasswordHash("hashedpassword");
        user.setEnabled(true);
        user.setEmail("testuser@example.com");
        user.setPhoneNumber("+1234567890");
        repository.save(user);
    }

    @Test
    void testFindByUsername() {
        Optional<AppUser> foundUser = repository.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void testFindByEmail() {
        Optional<AppUser> foundUser = repository.findByEmail("testuser@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByPhoneNumber() {
        Optional<AppUser> foundUser = repository.findByPhoneNumber("+1234567890");

        assertTrue(foundUser.isPresent());
        assertEquals("+1234567890", foundUser.get().getPhoneNumber());
    }

    @Test
    void testUpdatePasswordByUsername() {
        // Perform the update
        repository.updatePasswordByUsername("newPasswordHash", "testuser");

        // Flush and clear the persistence context to ensure we fetch fresh data
        entityManager.flush();
        entityManager.clear();

        // Verify the password was updated
        Optional<AppUser> foundUser = repository.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("newPasswordHash", foundUser.get().getPasswordHash());
    }


    @Test
    void testSaveAndRetrieveUserWithRolesAndTeams() {
        Role role = new Role("USER");
        roleRepository.save(role);

        Team team = new Team();
        team.setTeamName("Test Team");
        teamRepository.save(team);

        user.getRoles().add(role);
        user.getTeams().add(team);

        AppUser savedUser = repository.save(user);

        Optional<AppUser> foundUser = repository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(1, foundUser.get().getRoles().size());
        assertEquals(1, foundUser.get().getTeams().size());
    }
}
