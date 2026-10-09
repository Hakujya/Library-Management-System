package com.kaizen.lms.controller;

import com.kaizen.lms.dto.DashboardStatsDto;
import com.kaizen.lms.service.DashboardService;
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

