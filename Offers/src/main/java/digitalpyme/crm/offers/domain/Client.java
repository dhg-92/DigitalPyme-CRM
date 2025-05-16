package digitalpyme.crm.offers.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String email;

    private String company;

    @NotNull
    private String phone;

    @NotNull
    private String address;

    @NotNull
    private String city;

    @NotNull
    private Long zipCode;

    @NotNull
    private String country;
}
