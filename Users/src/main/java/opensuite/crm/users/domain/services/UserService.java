package opensuite.crm.users.domain.services;

import opensuite.crm.users.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();

    Optional<User> findUserById(Long idUser);

    Long createUser(User user);

    UserDetails loadUserByEmail(String idUser);

    Optional<User> findUserByEmail(String email);

    Boolean removeUser(Long idUser);
}
