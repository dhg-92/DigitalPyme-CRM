package digitalpyme.crm.notifications.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import digitalpyme.crm.notifications.domain.Notification;
import digitalpyme.crm.notifications.domain.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService  {

    private final NotificationRepository notificationRepository;

    @Override
    public Optional<Notification> findNotificationById(String id) {
        return notificationRepository.findNotificationById(id);
    }

    @Override
    public List<Notification> findAllNotification() {
        return notificationRepository.findAllNotification();
    }

    @Override
    public String createNotification(Notification notification) {
        return notificationRepository.createNotification(notification);
    }

    @Override
    public String updateNotification(Notification notification) {
        return notificationRepository.updateNotification(notification);
    }
}
