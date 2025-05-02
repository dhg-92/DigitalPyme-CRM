package opensuite.crm.users.application.rest.response;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class AuthResponse {
    private final String access_token;

    private final String token_type;

    private final long expires_in;

}
