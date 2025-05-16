package digitalpyme.crm.offers.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findClientByIdClient(Long idClient);
    Optional<ClientEntity> findClientByEmail(String email);
}
