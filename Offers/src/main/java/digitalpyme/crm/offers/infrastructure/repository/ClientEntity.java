package digitalpyme.crm.offers.infrastructure.repository;

import jakarta.persistence.*;
import lombok.*;
import digitalpyme.crm.offers.domain.Client;

@Entity(name = "Client")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity implements DomainTranslatable<Client> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", nullable = false, length = 100)
    private String surname;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "company", nullable = false, length = 100)
    private String company;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "zipCode", nullable = false, length = 255)
    private Long zipCode;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    public static ClientEntity fromDomain(Client client) {
        if (client == null) {
            return null;
        }

        return ClientEntity.builder()
                .idClient(client.getIdClient())
                .name(client.getName())
                .surname(client.getSurname())
                .email(client.getEmail())
                .company(client.getCompany())
                .phone(client.getPhone())
                .address(client.getAddress())
                .city(client.getCity())
                .zipCode(client.getZipCode())
                .country(client.getCountry())
                .build();
    }

    @Override
    public Client toDomain() {
        return Client.builder()
                .idClient(this.getIdClient())
                .name(this.getName())
                .surname(this.getSurname())
                .email(this.getEmail())
                .company(this.getCompany())
                .phone(this.getPhone())
                .address(this.getAddress())
                .city(this.getCity())
                .zipCode(this.getZipCode())
                .country(this.getCountry())
                .build();
    }
}
