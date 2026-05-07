package com.advising.scheduler.service;

import com.advising.scheduler.model.TimeSlot;
import com.advising.scheduler.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotService {

    private final TimeSlotRepository repo;

    public TimeSlotService(TimeSlotRepository repo) {
        this.repo = repo;
    }

    public List<TimeSlot> getOpenSlots() {
        return repo.findOpenSlots();
    }

    public List<TimeSlot> getAllSlots() {
        return repo.findAll();
    }

    public Optional<TimeSlot> getSlotById(Long slotId) {
        return repo.findById(slotId);
    }

    public void addSlot(String advisorName, String startTime, String endTime) {
        TimeSlot ts = new TimeSlot();
        ts.setAdvisorName(advisorName);
        ts.setStartTime(startTime);
        ts.setEndTime(endTime);
        ts.setOpen(true);
        repo.save(ts);
    }

    public boolean deleteSlot(Long id) {
        return repo.delete(id);
    }
}
