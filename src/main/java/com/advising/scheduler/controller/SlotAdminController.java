package com.advising.scheduler.controller;

import com.advising.scheduler.service.TimeSlotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/slots")
public class SlotAdminController {

    private final TimeSlotService svc;

    public SlotAdminController(TimeSlotService svc) {
        this.svc = svc;
    }

    @GetMapping
    public String view(Model model) {
        model.addAttribute("slots", svc.getAllSlots());
        return "admin/slots";
    }

    @PostMapping("/add")
    public String add(@RequestParam String advisorName,
                      @RequestParam String startTime,
                      @RequestParam String endTime,
                      RedirectAttributes redirect) {
        svc.addSlot(advisorName, startTime, endTime);
        redirect.addFlashAttribute("message", "Slot added successfully.");
        return "redirect:/admin/slots";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        if (svc.deleteSlot(id)) {
            redirect.addFlashAttribute("message", "Slot deleted.");
        } else {
            redirect.addFlashAttribute("error", "Cannot delete a slot that is already booked.");
        }
        return "redirect:/admin/slots";
    }
}
