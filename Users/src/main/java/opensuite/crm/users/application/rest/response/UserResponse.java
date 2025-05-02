package opensuite.crm.users.application.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import opensuite.crm.users.domain.User;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserResponse {
    private Long idUser;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private Boolean isAdmin;

    public static UserResponse userToGetUserResponse(User user) {
        return new UserResponse(
                user.getIdUser(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhone(),
                user.getIsAdmin()
        );
    }
}
