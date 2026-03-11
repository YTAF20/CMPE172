package com.advising.scheduler.repository;

import com.advising.scheduler.model.TimeSlot;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TimeSlotRepository {

    private final List<TimeSlot> slots = new ArrayList<>();

    public TimeSlotRepository() {
        slots.add(new TimeSlot(1L, "Dr. Dana Rivera", "2026-03-20 09:00", "2026-03-20 09:30"));
        slots.add(new TimeSlot(2L, "Dr. Dana Rivera", "2026-03-20 10:00", "2026-03-20 10:30"));
        slots.add(new TimeSlot(3L, "Dr. Dana Rivera", "2026-03-21 13:00", "2026-03-21 13:30"));
        slots.add(new TimeSlot(4L, "Dr. Eric Chen",   "2026-03-22 11:00", "2026-03-22 11:30"));
        slots.add(new TimeSlot(5L, "Dr. Eric Chen",   "2026-03-23 14:00", "2026-03-23 14:30"));
    }

    public List<TimeSlot> findOpenSlots() {
        return slots.stream().filter(TimeSlot::isOpen).collect(Collectors.toList());
    }

    public Optional<TimeSlot> findById(Long id) {
        return slots.stream().filter(s -> s.getSlotId().equals(id)).findFirst();
    }

    public void markSlotBooked(Long id) {
        findById(id).ifPresent(s -> s.setOpen(false));
    }
}
