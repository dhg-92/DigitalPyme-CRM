package opensuite.crm.offers.domain.services;

import opensuite.crm.offers.domain.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Optional<Client> findClientById(Long idClient);
    List<Client> findAllClients();
    Long createClient(Client client);
    Optional<Client> findClientByEmail(String email);
    Boolean removeClient(Long idClient);
}
