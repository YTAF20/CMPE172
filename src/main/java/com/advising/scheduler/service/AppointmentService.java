package com.advising.scheduler.service;

import com.advising.scheduler.model.Appointment;
import com.advising.scheduler.model.TimeSlot;
import com.advising.scheduler.repository.AppointmentRepository;
import com.advising.scheduler.repository.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                               TimeSlotRepository timeSlotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Transactional
    public boolean bookAppointment(Long slotId, Long studId) {
        logger.info("Attempting to book slotId={} for studId={}", slotId, studId);
        Optional<TimeSlot> slot = timeSlotRepository.findById(slotId);
        if (slot.isEmpty() || !slot.get().isOpen()) {
            logger.warn("Booking failed: slot id={} is not available", slotId);
            return false;
        }
        timeSlotRepository.markSlotBooked(slotId);
        appointmentRepository.save(slotId, studId);
        logger.info("Booking successful: slotId={}, studId={}", slotId, studId);
        return true;
    }

    @Transactional
    public boolean cancelAppointment(Long appId) {
        logger.info("Attempting to cancel appointment id={}", appId);
        Optional<Appointment> app = appointmentRepository.findBySlotId(appId);
        if (app.isEmpty()) {
            logger.warn("Cancel failed: appointment id={} not found", appId);
            return false;
        }
        appointmentRepository.updateStatus(appId, "CANCELLED");
        timeSlotRepository.markSlotOpen(app.get().getSlotId());
        logger.info("Appointment id={} cancelled", appId);
        return true;
    }

    public List<Appointment> getAllAppointments() {
        logger.info("Fetching all appointments");
        return appointmentRepository.findAll();
    }

    public List<Appointment> getStudentAppointments(Long studId) {
        logger.info("Fetching appointments for studId={}", studId);
        return appointmentRepository.findByStudentId(studId);
    }
}
