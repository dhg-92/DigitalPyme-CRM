package digitalpyme.crm.users.domain.repository;

import digitalpyme.crm.users.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAllUsers();

    Optional<User> findUserById(Long idUser);

    Optional<User> findUserByEmail(String email);

    Long createUser(User user);

    Boolean removeUser(Long idUser);

}
