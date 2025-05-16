package digitalpyme.crm.notifications.tests;

import digitalpyme.crm.notifications.domain.Notification;
import digitalpyme.crm.notifications.domain.SmtpConfig;
import digitalpyme.crm.notifications.domain.repository.NotificationRepository;
import digitalpyme.crm.notifications.domain.repository.SMTPConfigRepository;
import digitalpyme.crm.notifications.domain.services.NotificationServiceImpl;
import digitalpyme.crm.notifications.domain.services.SMTPConfigServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationsUnitTest {

    @InjectMocks
    private SMTPConfigServiceImpl smtpConfigService;

    @Mock
    private SMTPConfigRepository smtpConfigRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Test
    void unitTest_FindAllNotifications() {
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setEmailTo("dh92@uoc.edu");
        notification.setSubject("Subject");
        notification.setBody("Body");
        notification.setStatus("Status");
        notification.setErrorMessage("");
        notification.setDateShipment(LocalDateTime.now());

        when(notificationRepository.updateNotification(notification)).thenReturn(notification.getId());
        assertEquals(notification.getId(), notificationService.updateNotification(notification));

        when(notificationRepository.findAllNotification()).thenReturn(List.of(notification));
        List<Notification> result = notificationService.findAllNotification();
        assertEquals(notification.getId(), result.get(0).getId());
    }

    @Test
    void unitTest_UpdateSMTPConfig() {
        SmtpConfig smtpConfig = SmtpConfig.builder()
                .id(1L)
                .host("smtp.example.com")
                .protocol("smtp")
                .port(587)
                .username("'username'")
                .password("password")
                .useSSL(false)
                .auth(false)
                .build();

        when(smtpConfigRepository.updateConfig(smtpConfig)).thenReturn(true);
        when(smtpConfigRepository.findSMTPConfig()).thenReturn(Optional.of(smtpConfig));

        assertTrue(smtpConfigService.updateSMTPConfig(smtpConfig));
        assertEquals(smtpConfig, smtpConfigService.getSMTPConfig().get());
    }
}
