package opensuite.crm.notifications.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import opensuite.crm.notifications.application.rest.data.SendMailData;
import opensuite.crm.notifications.domain.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import opensuite.crm.notifications.infrastructure.repository.security.JwtUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class OfferListenerService {

    @Value("${offerServices.url}")
    private String urlofferServices;

    private final SMTPConfigService smtpConfigService;
    private final NotificationService notificationService;


    @KafkaListener(topics = "sendOffer", groupId = "opencrm-offer", containerFactory = "kafkaListenerOfferContainerFactory")
    public void listenOffer(SendMailData dataMail) {

        Optional<Notification> notificationData = notificationService.findNotificationById(dataMail.getInternalId());
        if (notificationData.isEmpty() || !notificationData.get().getStatus().equals("Enviada")) {
            Notification notification = new Notification();
            notification.setId(dataMail.getInternalId());
            notification.setOfferId(dataMail.getOfferId());
            notification.setEmailTo(dataMail.getEmailClient());
            notification.setSubject("Offer" + dataMail.getOfferId());
            notification.setBody("Offer" + dataMail.getOfferId());
            notification.setStatus("Enviando");
            notification.setErrorMessage("");
            notification.setDateShipment(LocalDateTime.now());
            notificationService.createNotification(notification);

            try {
                String url = urlofferServices.replace("{id}", dataMail.getOfferId().toString());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Authorization", "Bearer " + JwtUtil.generateToken());

                HttpEntity<String> entity = new HttpEntity<>(headers);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    byte[] pdfBytes = response.getBody();

                    String data = smtpConfigService.sendOfferByEmail(
                            dataMail.getEmailClient(),
                            "Tu oferta",
                            dataMail.getOfferId().toString(),
                            pdfBytes,
                            "Oferta-" + dataMail.getOfferId() + ".pdf"
                    );

                    if (data.equals("")) {
                        notification.setStatus("Enviada");
                        notificationService.updateNotification(notification);
                    } else {
                        notification.setStatus("Error en al enviar la oferta");
                        notification.setErrorMessage(data);
                        notificationService.updateNotification(notification);
                    }
                } else {
                    notification.setStatus("Error al descargar la oferta");
                    notification.setErrorMessage(response.getBody().toString());
                    notificationService.updateNotification(notification);
                }
            } catch (Exception e) {
                notification.setStatus("Error al obtener la oferta");
                notification.setErrorMessage(e.getMessage().toString());
                notificationService.updateNotification(notification);
            }
        } else {
            Notification notification = new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setOfferId(dataMail.getOfferId());
            notification.setEmailTo(dataMail.getEmailClient());
            notification.setSubject("Offer" + dataMail.getOfferId());
            notification.setBody("Offer" + dataMail.getOfferId());
            notification.setStatus("Duplicado");
            notification.setErrorMessage("Se detecta oferta previamente registrada");
            notification.setDateShipment(LocalDateTime.now());
            notificationService.createNotification(notification);
        }
    }
}
