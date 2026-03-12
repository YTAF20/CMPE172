package com.advising.scheduler.service;

import com.advising.scheduler.model.TimeSlot;
import com.advising.scheduler.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public List<TimeSlot> getOpenSlots() {
        return timeSlotRepository.findOpenSlots();
    }

    public Optional<TimeSlot> getSlotById(Long slotId) {
        return timeSlotRepository.findById(slotId);
    }
}
