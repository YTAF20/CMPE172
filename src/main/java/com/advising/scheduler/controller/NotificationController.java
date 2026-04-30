package com.advising.scheduler.controller;

import com.advising.scheduler.model.NotificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Mock external Notification Service.
 *
 * This controller simulates a coarse-grained external notification provider.
 * In production this would be a separate deployed service (e.g., SendGrid, AWS SES).
 * The single endpoint /notification-service/send accepts a full appointment payload
 * and returns a confirmation, hiding all internal email-delivery details from the caller.
 *
 * Coarse-grained justification: the Appointment Scheduler passes one rich request
 * object and receives one status response — no fine-grained calls for "prepare email",
 * "attach header", "submit job", etc. This reduces coupling and network chattiness.
 */
@RestController
@RequestMapping("/notification-service")
public class NotificationController {

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendConfirmation(
            @RequestBody NotificationRequest request) {

        // Simulate sending a confirmation email
        System.out.printf("[NotificationService] Sending confirmation to %s for appointment #%d " +
                "with %s at %s%n",
                request.getStudentName(),
                request.getApptId(),
                request.getAdvisorName(),
                request.getStartTime());

        return ResponseEntity.ok(Map.of(
                "status", "sent",
                "message", "Confirmation email sent to " + request.getStudentName()
        ));
    }
}
