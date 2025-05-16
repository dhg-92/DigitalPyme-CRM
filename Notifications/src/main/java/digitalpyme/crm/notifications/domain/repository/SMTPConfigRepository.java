package digitalpyme.crm.notifications.domain.repository;

import digitalpyme.crm.notifications.domain.SmtpConfig;

import java.util.Optional;

public interface SMTPConfigRepository {
    Optional<SmtpConfig> findSMTPConfig();
    Boolean updateConfig(SmtpConfig smtpConfig);
}
