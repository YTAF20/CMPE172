package com.advising.scheduler.controller;

import com.advising.scheduler.service.AppointmentService;
import com.advising.scheduler.service.TimeSlotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final AppointmentService apptSvc;
    private final TimeSlotService slotSvc;

    public HealthController(AppointmentService apptSvc, TimeSlotService slotSvc) {
        this.apptSvc = apptSvc;
        this.slotSvc = slotSvc;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("bookings_total", apptSvc.getSuccesses());
        response.put("bookings_failed", apptSvc.getFailures());
        response.put("bookings_attempted", apptSvc.getAttempts());
        response.put("slots_available", slotSvc.getOpenSlots().size());
        return response;
    }
}
