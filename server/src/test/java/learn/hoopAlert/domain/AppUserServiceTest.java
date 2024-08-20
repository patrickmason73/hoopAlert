package learn.hoopAlert.domain;

import learn.hoopAlert.data.AppUserRepository;
import learn.hoopAlert.data.RoleRepository;
import learn.hoopAlert.data.TeamRepository;
import learn.hoopAlert.models.AppUser;
import learn.hoopAlert.models.Reminder;
import learn.hoopAlert.models.Role;
import learn.hoopAlert.models.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository repository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private ReminderService reminderService;

    @InjectMocks
    private AppUserService appUserService;

    private AppUser user;
    private Team team;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPasswordHash("hashedpassword");
        user.setEnabled(true);
        user.setEmail("testuser@example.com");
        user.setPhoneNumber("+1234567890");
        user.setTeams(new HashSet<>());

        team = new Team();
        team.setId(1L);
        team.setTeamName("Test Team");
    }

    @Test
    void testAddTeamToUser_success() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        Result<Void> result = appUserService.addTeamToUser(1L, 1L);

        assertTrue(result.isSuccess());
        verify(repository, times(1)).save(user);
        verify(reminderService, times(1)).createRemindersForTeam(user, team);
    }

    @Test
    void testAddTeamToUser_userNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Result<Void> result = appUserService.addTeamToUser(1L, 1L);

        assertFalse(result.isSuccess());
        assertEquals("User not found", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
        verify(reminderService, never()).createRemindersForTeam(any(AppUser.class), any(Team.class));
    }

    @Test
    void testAddTeamToUser_teamNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        Result<Void> result = appUserService.addTeamToUser(1L, 1L);

        assertFalse(result.isSuccess());
        assertEquals("Team not found", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
        verify(reminderService, never()).createRemindersForTeam(any(AppUser.class), any(Team.class));
    }

    @Test
    void testAddTeamToUser_teamAlreadyAdded() {
        user.getTeams().add(team);
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        Result<Void> result = appUserService.addTeamToUser(1L, 1L);

        assertFalse(result.isSuccess());
        assertEquals("Team already added to the user", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
        verify(reminderService, never()).createRemindersForTeam(any(AppUser.class), any(Team.class));
    }

    @Test
    void testRemoveTeamFromUser_success() {
        user.getTeams().add(team);
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        Result<Void> result = appUserService.removeTeamFromUser(1L, 1L);

        assertTrue(result.isSuccess());
        verify(repository, times(1)).save(user);
        verify(reminderService, times(1)).deleteRemindersForTeam(user, team);
    }

    @Test
    void testRemoveTeamFromUser_userNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Result<Void> result = appUserService.removeTeamFromUser(1L, 1L);

        assertFalse(result.isSuccess());
        assertEquals("User not found", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
        verify(reminderService, never()).deleteRemindersForTeam(any(AppUser.class), any(Team.class));
    }

    @Test
    void testRemoveTeamFromUser_teamNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        Result<Void> result = appUserService.removeTeamFromUser(1L, 1L);

        assertFalse(result.isSuccess());
        assertEquals("Team not found", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
        verify(reminderService, never()).deleteRemindersForTeam(any(AppUser.class), any(Team.class));
    }

    @Test
    void testLoadUserByUsername_userFound() {
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(user));

        AppUser foundUser = appUserService.loadUserByUsername("testuser");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    void testLoadUserByUsername_userNotFound() {
        when(repository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> appUserService.loadUserByUsername("testuser"));
    }

    @Test
    void testCreate_success() {
        String password = "password";
        String encodedPassword = "encodedPassword";
        when(encoder.encode(password)).thenReturn(encodedPassword);
        when(roleRepository.findByName("USER")).thenReturn(new Role("USER"));
        when(repository.save(any(AppUser.class))).thenAnswer(i -> i.getArgument(0));

        Result<AppUser> result = appUserService.create("testuser", password, "+1234567890", "testuser@example.com");

        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals("testuser", result.getPayload().getUsername());
        assertEquals(encodedPassword, result.getPayload().getPasswordHash());
        verify(repository, times(1)).save(any(AppUser.class));
    }

    @Test
    void testCreate_failure_dueToDuplicateUsername() {
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Result<AppUser> result = appUserService.create("testuser", "password", "+1234567890", "testuser@example.com");

        assertFalse(result.isSuccess());
        assertEquals("Username already exists", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
    }

    @Test
    void testCreate_failure_dueToDuplicateEmail() {
        when(repository.findByEmail("testuser@example.com")).thenReturn(Optional.of(user));

        Result<AppUser> result = appUserService.create("testuser", "password", "+1234567890", "testuser@example.com");

        assertFalse(result.isSuccess());
        assertEquals("Email already exists", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
    }

    @Test
    void testCreate_failure_dueToDuplicatePhoneNumber() {
        when(repository.findByPhoneNumber("+1234567890")).thenReturn(Optional.of(user));

        Result<AppUser> result = appUserService.create("testuser", "password", "+1234567890", "testuser@example.com");

        assertFalse(result.isSuccess());
        assertEquals("Phone number already exists", result.getMessages().get(0));
        verify(repository, never()).save(any(AppUser.class));
    }
}
