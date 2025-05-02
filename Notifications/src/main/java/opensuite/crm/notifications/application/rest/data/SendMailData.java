package opensuite.crm.notifications.application.rest.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SendMailData {

    private String internalId;

    private Long offerId;

    private String emailClient;

    public SendMailData() {
    }
}
