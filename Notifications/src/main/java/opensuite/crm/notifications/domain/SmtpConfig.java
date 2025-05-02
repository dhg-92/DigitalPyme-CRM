package opensuite.crm.notifications.domain;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmtpConfig {

    private Long id;

    private String host;

    private String protocol;

    private Integer port;

    private String username;

    private String password;
    
    private Boolean useSSL;

    private Boolean auth;

}
