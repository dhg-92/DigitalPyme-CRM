package opensuite.crm.users.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAuthUserRequest {

    private final String email;

    private final String password;
}
