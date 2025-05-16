package digitalpyme.crm.users;

import digitalpyme.crm.users.domain.User;
import digitalpyme.crm.users.domain.repository.UserRepository;
import digitalpyme.crm.users.domain.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersUnitTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void unitTest_Users() {
        assertTrue(userService.findAllUsers().isEmpty());
        assertTrue(userService.findUserByEmail("example@example.com").isEmpty());

        User user = new User();
        user.setIdUser(5L);
        user.setName("Name");
        user.setSurname("Surname");
        user.setEmail("example@example.com");
        user.setPhone("123456");
        user.setIsAdmin(false);
        user.setValidRestoreToken(null);
        user.setRestoreToken(null);
        user.setPassword("123456");

        when(userRepository.createUser(user)).thenReturn(user.getIdUser());
        assertEquals(5L, userService.createUser(user));

        assertTrue(userService.findAllUsers().isEmpty());
        when(userRepository.findAllUsers()).thenReturn(List.of(user));
        assertFalse(userService.findAllUsers().isEmpty());

        assertTrue(userService.findUserById(user.getIdUser()).isEmpty());
        when(userRepository.findUserById(user.getIdUser())).thenReturn(Optional.of(user));
        assertFalse(userService.findUserById(user.getIdUser()).isEmpty());


        assertTrue(userService.findUserByEmail("example@example.com").isEmpty());
        when(userRepository.findUserByEmail("example@example.com")).thenReturn(Optional.of(user));
        assertFalse(userService.findUserByEmail("example@example.com").isEmpty());
    }
}