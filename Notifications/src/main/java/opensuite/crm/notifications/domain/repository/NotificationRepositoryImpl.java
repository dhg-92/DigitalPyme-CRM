package opensuite.crm.notifications.domain.repository;

import opensuite.crm.notifications.domain.Notification;
import opensuite.crm.notifications.infrastructure.repository.NotificationEntity;
import opensuite.crm.notifications.infrastructure.repository.SpringDataNotificationsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository{

    private final SpringDataNotificationsRepository jpaRepository;

    public NotificationRepositoryImpl(SpringDataNotificationsRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Notification> findNotificationById(String id) {
        return jpaRepository.findNotificationById(id).map(NotificationEntity::toDomain);
    }

    @Override
    public List<Notification> findAllNotification() {
        return jpaRepository.findAll().stream().map(NotificationEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public String createNotification(Notification notification) {
        return jpaRepository.save(NotificationEntity.fromDomain(notification)).getId();
    }

    @Override
    public String updateNotification(Notification notification) {
        return jpaRepository.save(NotificationEntity.fromDomain(notification)).getId();
    }


}
