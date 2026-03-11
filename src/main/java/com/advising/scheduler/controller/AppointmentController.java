package com.advising.scheduler.controller;

import com.advising.scheduler.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String viewAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "appointments";
    }

    @PostMapping("/book")
    public String bookAppointment(@RequestParam Long slotId,
                                   @RequestParam String studentName,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        boolean success = appointmentService.bookAppointment(slotId, studentName);
        if (!success) {
            redirectAttributes.addFlashAttribute("error", "This slot is no longer available. Please choose another.");
            return "redirect:/slots";
        }
        model.addAttribute("message", "Your appointment has been successfully booked!");
        return "confirmation";
    }
}
