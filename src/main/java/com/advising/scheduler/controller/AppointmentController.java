package com.advising.scheduler.controller;

import com.advising.scheduler.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping
    public String viewAppointments(Model model) {
        model.addAttribute("appointments", service.getAllAppointments());
        return "appointments";
    }

    @PostMapping("/book")
    public String bookAppointment(@RequestParam Long slotId,
                                   @RequestParam String studentName,
                                   Model model,
                                   RedirectAttributes redirect) {
        String result = service.bookAppointment(slotId, studentName);
        if (result == null) {
            redirect.addFlashAttribute("error", "This slot is no longer available. Please choose another.");
            return "redirect:/slots";
        }
        model.addAttribute("message", "Your appointment has been successfully booked!");
        model.addAttribute("notifStatus", result);
        return "confirmation";
    }
}
