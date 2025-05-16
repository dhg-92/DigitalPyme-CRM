package digitalpyme.crm.notifications.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import digitalpyme.crm.notifications.application.rest.data.RestorePasswordData;
import digitalpyme.crm.notifications.domain.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserListenerService {

    @Value("${app.resetPassword.url}")
    private String resetPasswordUrl;

    private final SMTPConfigService smtpConfigService;
    private final NotificationService notificationService;

    @KafkaListener(topics = "restorePassword", groupId = "opencrm-user", containerFactory = "kafkaListenerUserContainerFactory")
    public void listenOffer(RestorePasswordData restorePasswordData) {

        Optional<Notification> notificationData = notificationService.findNotificationById(restorePasswordData.getInternalId());
        if (notificationData.isEmpty() || !notificationData.get().getStatus().equals("Enviada")) {
            Notification notification = new Notification();
            notification.setId(restorePasswordData.getInternalId());
            notification.setEmailTo(restorePasswordData.getEmailUser());
            notification.setSubject("Restablecimiento de contraseña");
            notification.setBody("A continuación se adjunta el token...");
            notification.setStatus("Enviando");
            notification.setErrorMessage("");
            notification.setDateShipment(LocalDateTime.now());
            notificationService.createNotification(notification);

            try {
                String data = null;
                if (restorePasswordData.getTokenRestore() == null) {
                    data = smtpConfigService.confirmRestorePasswordUser(
                            restorePasswordData.getEmailUser(),
                            "Contraseña restablecida correctamente");
                } else {
                    data = smtpConfigService.sendLinkRestorePasswordUser(
                            restorePasswordData.getEmailUser(),
                            "Restablecimiento de contraseña",
                            resetPasswordUrl.replace("{tokenRestore}", restorePasswordData.getTokenRestore()));
                }
                if (data.equals("")) {
                    notification.setStatus("Enviada");
                    notificationService.updateNotification(notification);
                } else {
                    notification.setStatus("Error al enviar el token");
                    notification.setErrorMessage(data);
                    notificationService.updateNotification(notification);
                }

            } catch (Exception e) {
                notification.setStatus("Error al enviaer el correo");
                notification.setErrorMessage(e.getMessage().toString());
                notificationService.updateNotification(notification);
            }
        } else {
            Notification notification = new Notification();
            notification.setId(restorePasswordData.getInternalId());
            notification.setEmailTo(restorePasswordData.getEmailUser());
            notification.setSubject("Restablecimiento de contraseña duplicado");
            notification.setBody("");
            notification.setStatus("Duplicado");
            notification.setErrorMessage("Se detecta restablecimiento de contraseña duplicado");
            notification.setDateShipment(LocalDateTime.now());
            notificationService.createNotification(notification);
        }
    }
}
