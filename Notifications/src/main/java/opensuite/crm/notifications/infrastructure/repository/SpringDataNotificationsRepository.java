package opensuite.crm.notifications.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataNotificationsRepository  extends JpaRepository<NotificationEntity, Long> {
    Optional<NotificationEntity> findNotificationById(String id);

}
