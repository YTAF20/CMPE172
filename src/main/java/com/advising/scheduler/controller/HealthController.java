package com.advising.scheduler.controller;

import com.advising.scheduler.service.AppointmentService;
import com.advising.scheduler.service.TimeSlotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final AppointmentService appointmentService;
    private final TimeSlotService timeSlotService;

    public HealthController(AppointmentService appointmentService, TimeSlotService timeSlotService) {
        this.appointmentService = appointmentService;
        this.timeSlotService = timeSlotService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("bookings_total", appointmentService.getSuccessfulBookings());
        response.put("bookings_failed", appointmentService.getFailedBookings());
        response.put("bookings_attempted", appointmentService.getTotalAttempts());
        response.put("slots_available", timeSlotService.getOpenSlots().size());
        response.put("avg_booking_latency_ms", appointmentService.getAverageLatencyMs());
        return response;
    }
}
