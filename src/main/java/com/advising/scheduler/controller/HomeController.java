package com.advising.scheduler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String home() {
        logger.info("GET / - serving home page");
        return "index";
    }

    @GetMapping("/health")
    public String health() {
        logger.info("GET /health - health check requested");
        return "health";
    }
}
