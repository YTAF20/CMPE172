package com.advising.scheduler.controller;

import com.advising.scheduler.model.TimeSlot;
import com.advising.scheduler.service.TimeSlotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/slots")
public class SlotController {

    private final TimeSlotService svc;

    public SlotController(TimeSlotService svc) {
        this.svc = svc;
    }

    @GetMapping
    public String viewSlots(Model model) {
        model.addAttribute("slots", svc.getOpenSlots());
        return "slots";
    }

    @GetMapping("/{id}/book")
    public String bookForm(@PathVariable Long id, Model model) {
        Optional<TimeSlot> slot = svc.getSlotById(id);
        if (slot.isEmpty() || !slot.get().isOpen()) {
            return "redirect:/slots";
        }
        model.addAttribute("slot", slot.get());
        return "book";
    }
}
