package com.example.demo.controller;

import com.example.demo.dto.DashboardStatsDto;
import com.example.demo.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for library overview dashboard analytics.
 */
@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping({"", "/", "/dashboard"})
    public String showDashboard(Model model) {
        DashboardStatsDto stats = dashboardService.getDashboardStatistics();
        model.addAttribute("stats", stats);
        model.addAttribute("activeNav", "dashboard");
        return "dashboard";
    }
}

