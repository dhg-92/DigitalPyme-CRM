package digitalpyme.crm.offers.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class CreateClientRequest {

        private String name;

        private String surname;

        private String email;

        private String company;

        private String phone;

        private String address;

        private String city;

        private Long zipCode;

        private String country;

}
