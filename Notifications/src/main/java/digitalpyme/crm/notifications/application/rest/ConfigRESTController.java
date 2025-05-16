package digitalpyme.crm.notifications.application.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import digitalpyme.crm.notifications.application.rest.request.UpdateConfigurationRequest;
import digitalpyme.crm.notifications.domain.Notification;
import digitalpyme.crm.notifications.domain.SmtpConfig;
import digitalpyme.crm.notifications.domain.services.NotificationService;
import digitalpyme.crm.notifications.domain.services.SMTPConfigService;
import digitalpyme.crm.notifications.infrastructure.repository.security.JwtUtil;
import digitalpyme.crm.notifications.infrastructure.repository.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/config")
public class ConfigRESTController {

    private final SMTPConfigService smtpConfigService;
    private final NotificationService notificationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getConfiguration() {
        log.trace("getConfiguration");
        SmtpConfig smtpConfig = smtpConfigService.getSMTPConfig().get();
        smtpConfig.setPassword(null);
        return ResponseEntity.ok().body(smtpConfig);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateConfiguration(@RequestBody @NotNull@Valid UpdateConfigurationRequest updateConfigurationRequest) {
        log.trace("updateConfiguration");

        SmtpConfig config = smtpConfigService.getSMTPConfig().get();
        config.setHost(updateConfigurationRequest.getHost());
        config.setProtocol(updateConfigurationRequest.getProtocol());
        config.setPort(updateConfigurationRequest.getPort());
        config.setUsername(updateConfigurationRequest.getUsername());
        config.setPassword(updateConfigurationRequest.getPassword());
        config.setUseSSL(updateConfigurationRequest.getUseSSL());
        config.setAuth(updateConfigurationRequest.getAuth());

        if (smtpConfigService.updateSMTPConfig(config)){
            log.info("SMTP config updated");
            return ResponseEntity.ok().body(true);
        } else {
            log.info("SMTP config update failed");
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("/testMail")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> testMail(HttpServletRequest request) {
        log.info("testMail");

        try {
            User user = JwtUtil.extractUsername(request.getHeader("Authorization").substring(7));
            Notification notification = new Notification();
            String subject = "Mensaje de prueba";
            String data = smtpConfigService.testMail(user.getEmail(), subject);
            if (data.equals("")) {
                notification.setStatus("Enviando");
            } else {
                notification.setStatus("Error en al enviar test");
            }

            String token = UUID.randomUUID().toString();
            notification.setId(token);
            notification.setOfferId(null);
            notification.setEmailTo(user.getEmail());
            notification.setSubject(subject);
            notification.setErrorMessage(data);
            notification.setDateShipment(LocalDateTime.now());
            notificationService.createNotification(notification);

            log.info("testMail Ok");
            return ResponseEntity.ok().body(true);

        } catch (Exception e){
            log.info("testMail error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(false);
        }
    }
}
