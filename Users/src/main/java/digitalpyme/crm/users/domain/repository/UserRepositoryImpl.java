package digitalpyme.crm.users.domain.repository;

import digitalpyme.crm.users.domain.User;
import digitalpyme.crm.users.infrastructure.repository.SpringDataUserRepository;
import digitalpyme.crm.users.infrastructure.repository.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final SpringDataUserRepository jpaRepository;

    public UserRepositoryImpl(SpringDataUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<User> findAllUsers() {
        return jpaRepository.findAll().stream().map(UserEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return jpaRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return jpaRepository.findUserEntityByEmail(email).map(UserEntity::toDomain);
    }

    @Override
    public Long createUser(User user) {
        return jpaRepository.save(UserEntity.fromDomain(user)).getIdUser();
    }

    @Override
    public Boolean removeUser(Long idUser) {
        try {
            jpaRepository.delete(jpaRepository.findById(idUser).orElseThrow(IllegalArgumentException::new));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
