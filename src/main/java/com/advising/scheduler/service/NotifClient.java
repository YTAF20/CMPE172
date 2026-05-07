package com.advising.scheduler.service;

import com.advising.scheduler.model.NotifRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class NotifClient {

    private final RestTemplate http;
    private static final String URL = "http://localhost:8080/notification-service/send";

    public NotifClient(RestTemplate http) {
        this.http = http;
    }

    public String send(NotifRequest req) {
        try {
            Map resp = http.postForObject(URL, req, Map.class);
            if (resp != null && "sent".equals(resp.get("status"))) {
                return (String) resp.get("message");
            }
            return "Notification service returned an unexpected response.";
        } catch (Exception e) {
            return "Notification could not be sent: " + e.getMessage();
        }
    }
}
