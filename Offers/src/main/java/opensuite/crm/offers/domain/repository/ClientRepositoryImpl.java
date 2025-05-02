package opensuite.crm.offers.domain.repository;

import opensuite.crm.offers.domain.Client;
import opensuite.crm.offers.infrastructure.repository.ClientEntity;
import opensuite.crm.offers.infrastructure.repository.SpringDataClientRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final SpringDataClientRepository jpaRepository;

    public ClientRepositoryImpl(SpringDataClientRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Client> findClientById(Long idClient) {
        return jpaRepository.findClientByIdClient(idClient).map(ClientEntity::toDomain);
    }

    @Override
    public List<Client> findAllClients() {
        return jpaRepository.findAll().stream().map(ClientEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Long createClient(Client client) {
        return jpaRepository.save(ClientEntity.fromDomain(client)).getIdClient();
    }

    @Override
    public Optional<Client> findClientByEmail(String email) {
        return jpaRepository.findClientByEmail(email).map(ClientEntity::toDomain);
    }

    @Override
    public Boolean removeClient(Long idClient) {
        try {
            jpaRepository.delete(jpaRepository.findById(idClient).orElseThrow(IllegalArgumentException::new));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
