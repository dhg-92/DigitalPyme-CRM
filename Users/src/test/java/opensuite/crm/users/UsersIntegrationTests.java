package opensuite.crm.users;

import jakarta.transaction.Transactional;
import opensuite.crm.users.domain.User;
import opensuite.crm.users.domain.repository.UserRepository;
import opensuite.crm.users.domain.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({UserRepositoryImpl.class})
@Transactional
class UsersIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void integrationTest_Users() {

        assertFalse(userRepository.findAllUsers().isEmpty());

        User userTestData = new User();
        userTestData.setName("Name");
        userTestData.setSurname("Surname");
        userTestData.setEmail("example@example.com");
        userTestData.setPhone("123456");
        userTestData.setIsAdmin(false);
        userTestData.setValidRestoreToken(null);
        userTestData.setRestoreToken(null);
        userTestData.setPassword("123456");


        Long id = userRepository.createUser(userTestData);
        userTestData.setIdUser(id);

        Optional<User> user = userRepository.findUserById(id);

        assertThat(user).isNotEmpty();

        assertThat(user.get()).isNotNull();

        assertEquals(userTestData, user.get());

        assertFalse(userRepository.findUserByEmail(userTestData.getEmail()).isEmpty());
        assertTrue(userRepository.removeUser(id));
        assertTrue(userRepository.findUserById(userTestData.getIdUser()).isEmpty());

    }

}
