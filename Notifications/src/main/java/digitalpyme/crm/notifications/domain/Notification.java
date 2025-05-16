package digitalpyme.crm.notifications.domain;

import java.time.LocalDateTime;

import lombok.*;


@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private String id;

    private Long offerId;

    private String emailTo;

    private String subject;

    private String body;

    private String status;

    private String errorMessage;

    private LocalDateTime dateShipment;

}
