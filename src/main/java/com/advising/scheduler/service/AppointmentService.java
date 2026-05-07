package com.advising.scheduler.service;

import com.advising.scheduler.model.Appointment;
import com.advising.scheduler.model.NotifRequest;
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

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository apptRepo;
    private final TimeSlotRepository slotRepo;
    private final NotifClient notifClient;

    private final AtomicLong attempts = new AtomicLong(0);
    private final AtomicLong successes = new AtomicLong(0);
    private final AtomicLong failures = new AtomicLong(0);

    public AppointmentService(AppointmentRepository apptRepo,
                               TimeSlotRepository slotRepo,
                               NotifClient notifClient) {
        this.apptRepo = apptRepo;
        this.slotRepo = slotRepo;
        this.notifClient = notifClient;
    }

    @Transactional
    public String bookAppointment(Long slotId, String student) {
        attempts.incrementAndGet();
        logger.info("Booking attempt for student {} on slot {}", student, slotId);

        Optional<TimeSlot> found = slotRepo.findById(slotId);
        if (found.isEmpty() || !found.get().isOpen()) {
            failures.incrementAndGet();
            logger.warn("Slot {} not found or already booked", slotId);
            return null;
        }

        TimeSlot ts = found.get();
        if (!slotRepo.markBooked(slotId, ts.getVersion())) {
            failures.incrementAndGet();
            logger.warn("Version conflict on slot {}, someone else booked it", slotId);
            return null;
        }

        Appointment app = new Appointment();
        app.setSlotId(slotId);
        app.setStudentName(student);
        app.setAdvisorName(ts.getAdvisorName());
        app.setStartTime(ts.getStartTime());
        app.setEndTime(ts.getEndTime());
        app.setStatus("SCHEDULED");
        app.setCreateTime(LocalDateTime.now().toString());
        apptRepo.save(app);

        NotifRequest notif = new NotifRequest(
                student, ts.getAdvisorName(),
                ts.getStartTime(), ts.getEndTime(), app.getAppId()
        );

        String result;
        try {
            result = notifClient.send(notif);
        } catch (Exception e) {
            logger.error("Notification failed: {}", e.getMessage());
            result = "NOTIFICATION_ERROR";
        }

        successes.incrementAndGet();
        logger.info("Appointment {} booked for {}", app.getAppId(), student);

        return result;
    }

    @Transactional
    public boolean cancelAppointment(Long appId) {
        Optional<Appointment> found = apptRepo.findById(appId);
        if (found.isEmpty() || "CANCELLED".equals(found.get().getStatus())) {
            logger.warn("Appointment {} not found or already cancelled", appId);
            return false;
        }
        Appointment app = found.get();
        apptRepo.updateStatus(appId, "CANCELLED");
        slotRepo.markOpen(app.getSlotId());
        logger.info("Cancelled appointment {} for student {}", appId, app.getStudentName());
        return true;
    }

    public List<Appointment> getAllAppointments() {
        return apptRepo.findAll();
    }

    public long getAttempts() { return attempts.get(); }
    public long getSuccesses() { return successes.get(); }
    public long getFailures() { return failures.get(); }
}
