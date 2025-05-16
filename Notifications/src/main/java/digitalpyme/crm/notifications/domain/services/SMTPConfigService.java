package digitalpyme.crm.notifications.domain.services;

import digitalpyme.crm.notifications.domain.SmtpConfig;

import java.util.Optional;

public interface SMTPConfigService {
    Optional<SmtpConfig> getSMTPConfig();
    Boolean updateSMTPConfig(SmtpConfig smtpConfig);
    String sendOfferByEmail(String to, String subject, String idOffer, byte[] fileContent, String filename);
    String sendLinkRestorePasswordUser(String to, String subject, String text);
    String confirmRestorePasswordUser(String to, String subject);
    String testMail(String to, String subject);
}
