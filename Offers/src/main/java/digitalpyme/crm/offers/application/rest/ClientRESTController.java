package digitalpyme.crm.offers.application.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import digitalpyme.crm.offers.application.rest.request.CreateClientRequest;
import digitalpyme.crm.offers.domain.Client;
import digitalpyme.crm.offers.domain.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/clients")
public class ClientRESTController {

    private final ClientService clientService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Client> getAllClients() {
        log.trace("getAllClients");

        return clientService.findAllClients().stream().toList();
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody @Valid CreateClientRequest createClientRequest) {
        log.trace("createClient");

        Optional<Client> existingClient = clientService.findClientByEmail(createClientRequest.getEmail());

        if (existingClient.isPresent()) {
            log.trace("Client exist " + createClientRequest);
            return ResponseEntity.status(409).body("Client Already Exists.");
        }

        log.trace("Creating client " + createClientRequest);

        Client newClient = new Client();
        newClient.setName(createClientRequest.getName());
        newClient.setSurname(createClientRequest.getSurname());
        newClient.setEmail(createClientRequest.getEmail());
        newClient.setCompany(createClientRequest.getCompany());
        newClient.setPhone(createClientRequest.getPhone());
        newClient.setAddress(createClientRequest.getAddress());
        newClient.setCity(createClientRequest.getCity());
        newClient.setZipCode(createClientRequest.getZipCode());
        newClient.setCountry(createClientRequest.getCountry());

        Long clientId = clientService.createClient(newClient);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clientId)
                .toUri();
        return ResponseEntity.created(uri).body(clientId);
    }

    @PutMapping("/{idClient}")
    public ResponseEntity<?> updateClient(@RequestBody @Valid CreateClientRequest updateClientRequest, @PathVariable Long idClient) {
        log.trace("updateClient");

        Optional<Client> existingClient = clientService.findClientById(idClient);

        if (existingClient.isEmpty()) {
            log.trace("Client not exist " + updateClientRequest);
            return ResponseEntity.status(409).body("Client not Exists.");
        }

        log.trace("Update client " + updateClientRequest);

        Client updateClient = existingClient.get();
        updateClient.setName(updateClientRequest.getName());
        updateClient.setSurname(updateClientRequest.getSurname());
        updateClient.setEmail(updateClientRequest.getEmail());
        updateClient.setCompany(updateClientRequest.getCompany());
        updateClient.setPhone(updateClientRequest.getPhone());
        updateClient.setAddress(updateClientRequest.getAddress());
        updateClient.setCity(updateClientRequest.getCity());
        updateClient.setZipCode(updateClientRequest.getZipCode());
        updateClient.setCountry(updateClientRequest.getCountry());

        clientService.createClient(updateClient);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(idClient)
                .toUri();
        return ResponseEntity.created(uri).body(idClient);
    }

    @GetMapping("/{idClient}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Client> findClientById(@PathVariable @NotNull Long idClient) {
        log.info("findClient");

        return clientService.findClientById(idClient).map(client -> ResponseEntity.ok().body(client))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idClient}")
    public ResponseEntity<?> deleteClient(@PathVariable Long idClient){
        log.trace("deleteClient");

        Optional<Client> existingClient = clientService.findClientById(idClient);

        if (existingClient.isEmpty()) {
            log.trace("Client to deleted not exist");
            return ResponseEntity.status(409).body("Client not Exists.");
        }

        if (clientService.removeClient(idClient)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(409).body("Could not be removed. There are offers associated with this client.");
        }
    }
}
