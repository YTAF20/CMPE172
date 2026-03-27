package com.advising.scheduler.service;

import com.advising.scheduler.model.Appointment;
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

    public AppointmentService(AppointmentRepository apptRepo,
                               TimeSlotRepository slotRepo) {
        this.apptRepo = apptRepo;
        this.slotRepo = slotRepo;
    }

    @Transactional
    public boolean bookAppointment(Long slotId, String studentName) {
        Optional<TimeSlot> found = slotRepo.findById(slotId);
        if (found.isEmpty() || !found.get().isOpen()) return false;
        TimeSlot ts = found.get();
        if (!slotRepo.markBooked(slotId, ts.getVersion())) return false;

        Appointment app = new Appointment();
        app.setSlotId(slotId);
        app.setStudentName(studentName);
        app.setAdvisorName(ts.getAdvisorName());
        app.setStartTime(ts.getStartTime());
        app.setEndTime(ts.getEndTime());
        app.setStatus("SCHEDULED");
        app.setCreateTime(LocalDateTime.now().toString());
        apptRepo.save(app);
        return true;
    }

    public List<Appointment> getAllAppointments() {
        return apptRepo.findAll();
    }
}
