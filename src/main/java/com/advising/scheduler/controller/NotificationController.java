package com.advising.scheduler.controller;

import com.advising.scheduler.model.NotifRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notification-service")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendConfirmation(@RequestBody NotifRequest request) {
        logger.info("Sending confirmation to {} for appointment {} with {} at {}",
                request.getStudentName(), request.getApptId(),
                request.getAdvisorName(), request.getStartTime());

        return ResponseEntity.ok(Map.of(
                "status", "sent",
                "message", "Confirmation email sent to " + request.getStudentName()
        ));
    }
}
