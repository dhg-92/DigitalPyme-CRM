package opensuite.crm.notifications.tests;

import jakarta.transaction.Transactional;
import opensuite.crm.notifications.domain.Notification;
import opensuite.crm.notifications.domain.SmtpConfig;
import opensuite.crm.notifications.domain.repository.NotificationRepository;
import opensuite.crm.notifications.domain.repository.NotificationRepositoryImpl;
import opensuite.crm.notifications.domain.repository.SMTPConfigRepository;
import opensuite.crm.notifications.domain.repository.SMTPConfigRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({SMTPConfigRepositoryImpl.class, NotificationRepositoryImpl.class})
@Transactional
class NotificationsIntegrationTests {

    @Autowired
    private SMTPConfigRepository smtpConfigRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void integrationTest_SMTPConfigs() {
        SmtpConfig smtpConfigTestData = SmtpConfig.builder()
                .id(1L)
                .host("smtp.example.com")
                .protocol("smtp")
                .port(587)
                .username("'username'")
                .password("password")
                .useSSL(false)
                .auth(false)
                .build();

        smtpConfigRepository.updateConfig(smtpConfigTestData);

        Optional<SmtpConfig> smtpConfig = smtpConfigRepository.findSMTPConfig();

        assertThat(smtpConfig).isNotEmpty();

        assertThat(smtpConfig.get()).isNotNull();

        assertEquals(smtpConfigTestData, smtpConfig.get());

    }

    @Test
    void integrationTest_Notifications() {
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setEmailTo("dh92@uoc.edu");
        notification.setSubject("Subject");
        notification.setBody("Body");
        notification.setStatus("Status");
        notification.setErrorMessage("");
        notification.setDateShipment(LocalDateTime.now());

        assertTrue(notificationRepository.findAllNotification().isEmpty());
        assertTrue(notificationRepository.findNotificationById(notification.getId()).isEmpty());

        assertEquals(notification.getId(), notificationRepository.createNotification(notification));

        assertFalse(notificationRepository.findAllNotification().isEmpty());

        notification.setStatus("Status-Saved");

        assertEquals(notification.getId(), notificationRepository.updateNotification(notification));

        Optional<Notification> notificationOptional = notificationRepository.findNotificationById(notification.getId());
        assertFalse(notificationOptional.isEmpty());
        assertEquals(notification.getStatus(), notificationOptional.get().getStatus());
    }
}
