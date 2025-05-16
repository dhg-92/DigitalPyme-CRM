package digitalpyme.crm.users.infrastructure.repository;

import jakarta.persistence.*;
import lombok.*;
import digitalpyme.crm.users.domain.User;

import java.time.LocalDateTime;

@Entity(name = "User")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`user`")
public class UserEntity implements DomainTranslatable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", nullable = false, length = 100)
    private String surname;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = true, length = 255)
    private String password;

    @Column(name = "phone", nullable = true, length = 15)
    private String phone;

    @Column(name = "isAdmin", nullable = false)
    private Boolean isAdmin;

    @Column(name = "restoreToken", nullable = true, length = 36)
    private String restoreToken;

    @Column(name = "validrestoreToken", nullable = true)
    private LocalDateTime validRestoreToken;

    public static UserEntity fromDomain(User user) {
        if (user == null) {
            return null;
        }

        return UserEntity.builder()
                .idUser(user.getIdUser())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .isAdmin(user.getIsAdmin())
                .restoreToken(user.getRestoreToken())
                .validRestoreToken(user.getValidRestoreToken())
                .build();
    }

    @Override
    public User toDomain() {
        return User.builder()
                .idUser(this.getIdUser())
                .name(this.getName())
                .surname(this.getSurname())
                .email(this.getEmail())
                .password(this.getPassword())
                .phone(this.getPhone())
                .isAdmin(this.getIsAdmin())
                .restoreToken(this.getRestoreToken())
                .validRestoreToken(this.getValidRestoreToken())
                .build();
    }
}
