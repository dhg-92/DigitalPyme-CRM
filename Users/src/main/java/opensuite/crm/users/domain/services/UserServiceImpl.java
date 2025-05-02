package opensuite.crm.users.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import opensuite.crm.users.domain.User;
import opensuite.crm.users.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Optional<User> findUserById(Long idUser) {
        return userRepository.findUserById(idUser);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Boolean removeUser(Long idUser) {
        return userRepository.removeUser(idUser);
    }

    public Long createUser(User user) {
        return userRepository.createUser(user);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return findUserByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        new ArrayList<>()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
