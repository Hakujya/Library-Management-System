package com.kaizen.lms.service;

import com.kaizen.lms.dto.DashboardStatsDto;

/**
 * Service interface for computing real-time dashboard analytics.
 */
public interface DashboardService {

    DashboardStatsDto getDashboardStatistics();
}

