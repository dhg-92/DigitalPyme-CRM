package digitalpyme.crm.users.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestorePasswordRequest {
    private String email;
}
