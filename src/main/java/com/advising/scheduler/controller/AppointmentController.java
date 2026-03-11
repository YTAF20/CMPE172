package com.advising.scheduler.controller;

import com.advising.scheduler.service.AppointmentService;
import com.advising.scheduler.service.TimeSlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    private final AppointmentService appointmentService;
    private final TimeSlotService slotService;

    public AppointmentController(AppointmentService appointmentService,
                                  TimeSlotService slotService) {
        this.appointmentService = appointmentService;
        this.slotService = slotService;
    }

    @GetMapping
    public String viewAppointments(Model model) {
        logger.info("GET /appointments - listing all appointments");
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "appointments";
    }

    @PostMapping("/book")
    public String bookAppointment(@RequestParam Long slotId,
                                   @RequestParam Long studId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        logger.info("POST /appointments/book - slotId={}, studId={}", slotId, studId);
        boolean success = appointmentService.bookAppointment(slotId, studId);
        if (!success) {
            redirectAttributes.addFlashAttribute("error", "This slot is no longer available. Please choose another.");
            return "redirect:/slots";
        }
        model.addAttribute("message", "Your appointment has been successfully booked!");
        return "confirmation";
    }

    @PostMapping("/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("POST /appointments/{}/cancel", id);
        boolean success = appointmentService.cancelAppointment(id);
        if (success) {
            redirectAttributes.addFlashAttribute("message", "Appointment cancelled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Could not cancel appointment.");
        }
        return "redirect:/appointments";
    }
}
