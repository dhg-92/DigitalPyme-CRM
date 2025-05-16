package digitalpyme.crm.offers.application.rest.data;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SendMailData {

    private String internalId;

    private Long offerId;

    private String emailClient;

}
