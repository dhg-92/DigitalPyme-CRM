package opensuite.crm.notifications.domain.services;

import opensuite.crm.notifications.domain.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Optional<Notification> findNotificationById(String id);
    List<Notification> findAllNotification();
    String createNotification(Notification notification);
    String updateNotification(Notification notification);
}
