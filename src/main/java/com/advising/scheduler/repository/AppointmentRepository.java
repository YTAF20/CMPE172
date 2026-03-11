package com.advising.scheduler.repository;

import com.advising.scheduler.model.Appointment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AppointmentRepository {

    private final List<Appointment> appointments = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public void save(Appointment appointment) {
        appointment.setAppId(nextId.getAndIncrement());
        appointments.add(appointment);
    }

    public List<Appointment> findAll() {
        return Collections.unmodifiableList(appointments);
    }
}
