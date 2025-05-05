package opensuite.crm.notifications.tests;

import opensuite.crm.notifications.domain.Notification;
import opensuite.crm.notifications.domain.SmtpConfig;
import opensuite.crm.notifications.domain.services.NotificationService;
import opensuite.crm.notifications.domain.services.SMTPConfigService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Mock
    private SMTPConfigService smtpConfigService;

    @Mock
    private NotificationService notificationService;

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

        when(notificationService.updateNotification(notification)).thenReturn(notification.getId());
        assertEquals(notification.getId(), notificationService.updateNotification(notification));

        when(notificationService.findAllNotification()).thenReturn(List.of(notification));
        List<Notification> result = notificationService.findAllNotification();
        assertEquals(notification.getId(), result.get(0).getId());
    }

    @Test
    void unitTest_testMail_InvalidData() {
        when(smtpConfigService.testMail("dh92@uoc.edu", "Unit Test")).thenReturn("");
        String result = smtpConfigService.testMail("dh92@uoc.edu", "Unit Test");
        assertEquals("", result);
    }

    @Test
    void unitTest_testMail_CorrectData() {
        when(smtpConfigService.testMail("dh92.uoc.edu", "Unit Test")).thenReturn("Traza: Invalid to email...");
        String result = smtpConfigService.testMail("dh92.uoc.edu", "Unit Test");
        assertTrue(result.startsWith("Traza: Invalid to email"));
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

        when(smtpConfigService.updateSMTPConfig(smtpConfig)).thenReturn(true);
        when(smtpConfigService.getSMTPConfig()).thenReturn(Optional.of(smtpConfig));

        assertTrue(smtpConfigService.updateSMTPConfig(smtpConfig));
        assertEquals(smtpConfig, smtpConfigService.getSMTPConfig().get());
    }
}
