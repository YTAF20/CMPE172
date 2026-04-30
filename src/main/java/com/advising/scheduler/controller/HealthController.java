package com.advising.scheduler.controller;

import com.advising.scheduler.service.AppointmentService;
import com.advising.scheduler.service.TimeSlotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final AppointmentService apptService;
    private final TimeSlotService slotService;

    public HealthController(AppointmentService apptService, TimeSlotService slotService) {
        this.apptService = apptService;
        this.slotService = slotService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("bookings_total",     apptService.getSuccesses());
        response.put("bookings_failed",    apptService.getFailures());
        response.put("bookings_attempted", apptService.getAttempts());
        response.put("slots_available",    slotService.getOpenSlots().size());
        return response;
    }
}
