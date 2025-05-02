package opensuite.crm.users.application.rest.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateUserRequest {

    private String name;

    private String surname;

    private String email;

    private String phone;

    private Boolean isAdmin;
}
