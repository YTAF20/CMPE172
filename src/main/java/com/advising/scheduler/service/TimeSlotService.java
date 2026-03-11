package com.advising.scheduler.service;

import com.advising.scheduler.model.TimeSlot;
import com.advising.scheduler.repository.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotService {

    private static final Logger logger = LoggerFactory.getLogger(TimeSlotService.class);

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public List<TimeSlot> getOpenSlots() {
        logger.info("Fetching all open time slots");
        return timeSlotRepository.findOpenSlots();
    }

    public Optional<TimeSlot> getSlotById(Long slotId) {
        logger.info("Fetching time slot id={}", slotId);
        return timeSlotRepository.findById(slotId);
    }
}
