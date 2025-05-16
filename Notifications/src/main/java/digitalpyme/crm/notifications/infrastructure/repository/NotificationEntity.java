package digitalpyme.crm.notifications.infrastructure.repository;

import jakarta.persistence.*;
import lombok.*;
import digitalpyme.crm.notifications.domain.Notification;

import java.time.LocalDateTime;

@Entity(name = "Notification")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity implements DomainTranslatable<Notification> {

    @Id
    @Column(unique = true)
    private String id;

    @Column(name = "offerId", nullable = true)
    private Long offerId;

    @Column(name = "emailTo", nullable = false, length = 100)
    private String emailTo;

    @Column(name = "subject", nullable = false, length = 100)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name = "status", nullable = false, length = 100)
    private String status;

    @Column(name = "errorMessage", nullable = true, columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "dateShipment", nullable = false)
    private LocalDateTime dateShipment;

    public static NotificationEntity fromDomain(Notification notification) {
        if (notification == null) {
            return null;
        }
        return NotificationEntity .builder()
                .id(notification.getId())
                .offerId(notification.getOfferId())
                .emailTo(notification.getEmailTo())
                .subject(notification.getSubject())
                .body(notification.getBody())
                .status(notification.getStatus())
                .errorMessage(notification.getErrorMessage())
                .dateShipment(notification.getDateShipment())
                .build();
    }

    public Notification toDomain() {
        return Notification.builder()
                .id(this.getId())
                .offerId(this.getOfferId())
                .emailTo(this.getEmailTo())
                .subject(this.getSubject())
                .body(this.getBody())
                .status(this.getStatus())
                .errorMessage(this.getErrorMessage())
                .dateShipment(this.getDateShipment())
                .build();
    }
}
