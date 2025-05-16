package digitalpyme.crm.offers.domain.repository;

import digitalpyme.crm.offers.domain.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Optional<Client> findClientById(Long idOffer);

    List<Client> findAllClients();

    Long createClient(Client client);

    Optional<Client> findClientByEmail(String email);

    Boolean removeClient(Long idClient);
}
