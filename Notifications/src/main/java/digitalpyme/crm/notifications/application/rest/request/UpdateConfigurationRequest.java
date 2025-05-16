package digitalpyme.crm.notifications.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateConfigurationRequest {

        private String host;

        private String protocol;

        private Integer port;

        private String username;

        private String password;

        private Boolean useSSL;

        private Boolean auth;
}
