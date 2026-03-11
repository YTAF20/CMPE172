package com.advising.scheduler.service;

import com.advising.scheduler.model.Appointment;
import com.advising.scheduler.model.TimeSlot;
import com.advising.scheduler.repository.AppointmentRepository;
import com.advising.scheduler.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                               TimeSlotRepository timeSlotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public boolean bookAppointment(Long slotId, String studentName) {
        Optional<TimeSlot> slot = timeSlotRepository.findById(slotId);
        if (slot.isEmpty() || !slot.get().isOpen()) {
            return false;
        }
        timeSlotRepository.markSlotBooked(slotId);

        Appointment app = new Appointment();
        app.setSlotId(slotId);
        app.setStudentName(studentName);
        app.setAdvisorName(slot.get().getAdvisorName());
        app.setStartTime(slot.get().getStartTime());
        app.setEndTime(slot.get().getEndTime());
        app.setStatus("SCHEDULED");
        app.setCreateTime(LocalDateTime.now().toString());
        appointmentRepository.save(app);
        return true;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
