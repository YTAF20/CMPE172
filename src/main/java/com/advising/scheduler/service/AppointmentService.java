package com.advising.scheduler.service;

import com.advising.scheduler.model.Appointment;
import com.advising.scheduler.model.NotificationRequest;
import com.advising.scheduler.model.TimeSlot;
import com.advising.scheduler.repository.AppointmentRepository;
import com.advising.scheduler.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository apptRepo;
    private final TimeSlotRepository slotRepo;
    private final NotificationClient notificationClient;

    public AppointmentService(AppointmentRepository apptRepo,
                               TimeSlotRepository slotRepo,
                               NotificationClient notificationClient) {
        this.apptRepo = apptRepo;
        this.slotRepo = slotRepo;
        this.notificationClient = notificationClient;
    }

    /**
     * Books an appointment and sends a confirmation via the external Notification Service.
     *
     * @return notification status message, or null if the slot was unavailable
     */
    @Transactional
    public String bookAppointment(Long slotId, String studentName) {
        Optional<TimeSlot> found = slotRepo.findById(slotId);
        if (found.isEmpty() || !found.get().isOpen()) return null;
        TimeSlot ts = found.get();
        if (!slotRepo.markBooked(slotId, ts.getVersion())) return null;

        Appointment app = new Appointment();
        app.setSlotId(slotId);
        app.setStudentName(studentName);
        app.setAdvisorName(ts.getAdvisorName());
        app.setStartTime(ts.getStartTime());
        app.setEndTime(ts.getEndTime());
        app.setStatus("SCHEDULED");
        app.setCreateTime(LocalDateTime.now().toString());
        apptRepo.save(app);

        // Delegate notification to the external Notification Service
        NotificationRequest notif = new NotificationRequest(
                studentName,
                ts.getAdvisorName(),
                ts.getStartTime(),
                ts.getEndTime(),
                app.getAppId()
        );
        return notificationClient.sendConfirmation(notif);
    }

    public List<Appointment> getAllAppointments() {
        return apptRepo.findAll();
    }
}
