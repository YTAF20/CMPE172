package com.advising.scheduler.service;

import com.advising.scheduler.model.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Client component that calls the external Notification Service.
 *
 * This is the boundary crossing point: the Appointment Scheduler delegates
 * all notification concerns to the external service via a single REST call.
 */
@Service
public class NotificationClient {

    private final RestTemplate restTemplate;
    private static final String NOTIFICATION_URL =
            "http://localhost:8080/notification-service/send";

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Send a booking confirmation through the external notification service.
     *
     * @return status message from the notification service, or an error message on failure
     */
    @SuppressWarnings("unchecked")
    public String sendConfirmation(NotificationRequest request) {
        try {
            Map<String, String> response = restTemplate.postForObject(
                    NOTIFICATION_URL, request, Map.class);
            if (response != null && "sent".equals(response.get("status"))) {
                return response.get("message");
            }
            return "Notification service returned an unexpected response.";
        } catch (Exception e) {
            return "Notification could not be sent: " + e.getMessage();
        }
    }
}
