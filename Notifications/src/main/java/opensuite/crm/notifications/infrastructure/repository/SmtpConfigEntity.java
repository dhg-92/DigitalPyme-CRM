package opensuite.crm.notifications.infrastructure.repository;

import jakarta.persistence.*;
import lombok.*;
import opensuite.crm.notifications.domain.SmtpConfig;

@Entity(name = "SmtpConfig")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmtpConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "host", nullable = false, length = 255)
    private String host;

    @Column(name = "protocol", nullable = false, length = 50)
    private String protocol;

    @Column(name = "port", nullable = false)
    private Integer port;

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "auth", nullable = false)
    private Boolean auth;

    @Column(name = "use_ssl", nullable = false)
    private Boolean useSSL;

    public static SmtpConfigEntity fromDomain(SmtpConfig smtpConfig) {
        if (smtpConfig == null) {
            return null;
        }

        return SmtpConfigEntity.builder()
                .id(smtpConfig.getId())
                .host(smtpConfig.getHost())
                .port(smtpConfig.getPort())
                .username(smtpConfig.getUsername())
                .password(smtpConfig.getPassword())
                .auth(smtpConfig.getAuth())
                .protocol(smtpConfig.getProtocol())
                .useSSL(smtpConfig.getUseSSL())
                .build();
    }

    public SmtpConfig toDomain() {
        return SmtpConfig.builder()
                .id(this.getId())
                .host(this.getHost())
                .port(this.getPort())
                .username(this.getUsername())
                .password(this.getPassword())
                .auth(this.getAuth())
                .protocol(this.getProtocol())
                .useSSL(this.getUseSSL())
                .build();
    }
}