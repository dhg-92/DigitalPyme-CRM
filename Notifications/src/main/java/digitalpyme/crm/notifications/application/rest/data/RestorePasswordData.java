package digitalpyme.crm.notifications.application.rest.data;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class RestorePasswordData {

    private String internalId;

    private String emailUser;

    private String tokenRestore;

    public RestorePasswordData() {
    }
}
