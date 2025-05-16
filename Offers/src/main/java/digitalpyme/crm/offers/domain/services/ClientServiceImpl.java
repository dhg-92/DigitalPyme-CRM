package digitalpyme.crm.offers.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import digitalpyme.crm.offers.domain.Client;
import digitalpyme.crm.offers.domain.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Optional<Client> findClientById(Long idClient) {
        return clientRepository.findClientById(idClient);
    }

    @Override
    public List<Client> findAllClients(){
        return clientRepository.findAllClients();
    }

    public Long createClient(Client client) {
        return clientRepository.createClient(client);
    }

    @Override
    public Optional<Client> findClientByEmail(String email) {
        return clientRepository.findClientByEmail(email);
    }

    @Override
    public Boolean removeClient(Long idClient) {
        return clientRepository.removeClient(idClient);
    }
}
