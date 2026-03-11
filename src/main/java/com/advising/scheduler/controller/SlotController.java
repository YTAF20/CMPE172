package com.advising.scheduler.controller;

import com.advising.scheduler.model.TimeSlot;
import com.advising.scheduler.service.TimeSlotService;
import com.advising.scheduler.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/slots")
public class SlotController {

    private static final Logger logger = LoggerFactory.getLogger(SlotController.class);

    private final TimeSlotService slotService;
    private final UserService userService;

    public SlotController(TimeSlotService slotService, UserService userService) {
        this.slotService = slotService;
        this.userService = userService;
    }

    @GetMapping
    public String viewSlots(Model model) {
        logger.info("GET /slots - listing open slots");
        model.addAttribute("slots", slotService.getOpenSlots());
        return "slots";
    }

    @GetMapping("/{id}/book")
    public String bookForm(@PathVariable Long id, Model model) {
        logger.info("GET /slots/{}/book - showing booking form", id);
        Optional<TimeSlot> slot = slotService.getSlotById(id);
        if (slot.isEmpty() || !slot.get().isOpen()) {
            logger.warn("Slot id={} not available for booking", id);
            return "redirect:/slots";
        }
        model.addAttribute("slot", slot.get());
        model.addAttribute("students", userService.getStudents());
        return "book";
    }
}
