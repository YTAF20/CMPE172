package com.advising.scheduler.service;

import com.advising.scheduler.model.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class NotificationClient {

    private final RestTemplate restTemplate;
    private static final String URL = "http://localhost:8080/notification-service/send";

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public String send(NotificationRequest req) {
        try {
            Map<String, String> resp = restTemplate.postForObject(URL, req, Map.class);
            if (resp != null && "sent".equals(resp.get("status"))) {
                return resp.get("message");
            }
            return "Notification service returned an unexpected response.";
        } catch (Exception e) {
            return "Notification could not be sent: " + e.getMessage();
        }
    }
}
