package digitalpyme.crm.users.application.rest.data;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class RestorePasswordData {

    private String internalId;

    private String emailUser;

    private String tokenRestore;
}
