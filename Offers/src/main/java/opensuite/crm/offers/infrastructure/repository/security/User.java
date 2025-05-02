package opensuite.crm.offers.infrastructure.repository.security;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String email;

    @NotNull
    private Boolean isAdmin;
}