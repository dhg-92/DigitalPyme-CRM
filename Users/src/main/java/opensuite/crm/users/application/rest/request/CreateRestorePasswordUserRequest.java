package opensuite.crm.users.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRestorePasswordUserRequest {

    private final String email;

    private final String restoreToken;

    private final String password;
}