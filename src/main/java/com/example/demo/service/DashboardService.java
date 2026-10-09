package com.example.demo.service;

import com.example.demo.dto.DashboardStatsDto;

/**
 * Service interface for computing real-time dashboard analytics.
 */
public interface DashboardService {

    DashboardStatsDto getDashboardStatistics();
}

