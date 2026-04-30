package com.advising.scheduler.service;

import com.advising.scheduler.model.Appointment;
import com.advising.scheduler.model.NotificationRequest;
import com.advising.scheduler.model.TimeSlot;
import com.advising.scheduler.repository.AppointmentRepository;
import com.advising.scheduler.repository.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository apptRepo;
    private final TimeSlotRepository slotRepo;
    private final NotificationClient notifClient;

    private final AtomicLong totalAttempts = new AtomicLong(0);
    private final AtomicLong successfulBookings = new AtomicLong(0);
    private final AtomicLong failedBookings = new AtomicLong(0);
    private final AtomicLong totalLatencyMs = new AtomicLong(0);

    public AppointmentService(AppointmentRepository apptRepo,
                               TimeSlotRepository slotRepo,
                               NotificationClient notifClient) {
        this.apptRepo = apptRepo;
        this.slotRepo = slotRepo;
        this.notifClient = notifClient;
    }

    @Transactional
    public String bookAppointment(Long slotId, String studentName) {
        long start = System.currentTimeMillis();
        totalAttempts.incrementAndGet();
        log.info("Booking attempt: student={} slotId={}", studentName, slotId);

        Optional<TimeSlot> found = slotRepo.findById(slotId);
        if (found.isEmpty() || !found.get().isOpen()) {
            failedBookings.incrementAndGet();
            log.warn("Booking failed: slot {} not found or already booked", slotId);
            return null;
        }

        TimeSlot ts = found.get();
        if (!slotRepo.markBooked(slotId, ts.getVersion())) {
            failedBookings.incrementAndGet();
            log.warn("Booking failed: version conflict on slot {} (concurrent booking detected)", slotId);
            return null;
        }

        Appointment app = new Appointment();
        app.setSlotId(slotId);
        app.setStudentName(studentName);
        app.setAdvisorName(ts.getAdvisorName());
        app.setStartTime(ts.getStartTime());
        app.setEndTime(ts.getEndTime());
        app.setStatus("SCHEDULED");
        app.setCreateTime(LocalDateTime.now().toString());
        apptRepo.save(app);

        NotificationRequest notif = new NotificationRequest(
                studentName, ts.getAdvisorName(),
                ts.getStartTime(), ts.getEndTime(), app.getAppId()
        );

        String notifResult;
        try {
            notifResult = notifClient.send(notif);
        } catch (Exception e) {
            log.error("Notification failed for appointment {}: {}", app.getAppId(), e.getMessage());
            notifResult = "NOTIFICATION_ERROR";
        }

        successfulBookings.incrementAndGet();
        long elapsed = System.currentTimeMillis() - start;
        totalLatencyMs.addAndGet(elapsed);
        log.info("Booking confirmed: appointmentId={} student={} advisor={} latencyMs={}",
                app.getAppId(), studentName, ts.getAdvisorName(), elapsed);

        return notifResult;
    }

    public List<Appointment> getAllAppointments() {
        return apptRepo.findAll();
    }

    public long getTotalAttempts()      { return totalAttempts.get(); }
    public long getSuccessfulBookings() { return successfulBookings.get(); }
    public long getFailedBookings()     { return failedBookings.get(); }

    public double getAverageLatencyMs() {
        long successes = successfulBookings.get();
        return successes == 0 ? 0.0 : (double) totalLatencyMs.get() / successes;
    }
}
