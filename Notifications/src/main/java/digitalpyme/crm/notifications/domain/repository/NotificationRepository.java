package digitalpyme.crm.notifications.domain.repository;

import digitalpyme.crm.notifications.domain.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Optional<Notification> findNotificationById(String id);

    List<Notification> findAllNotification();

    String createNotification(Notification notification);

    String updateNotification(Notification notification);
}
