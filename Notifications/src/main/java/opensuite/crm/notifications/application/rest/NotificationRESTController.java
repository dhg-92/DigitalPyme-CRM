package opensuite.crm.notifications.application.rest;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import opensuite.crm.notifications.domain.Notification;
import opensuite.crm.notifications.domain.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/notifications")
public class NotificationRESTController {

    private final NotificationService notificationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> getAllNotifications() {
        log.trace("getAllNotifications");

        return notificationService.findAllNotification();
    }

}
