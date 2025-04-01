package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.common.Result;
import com.tencent.wxcloudrun.service.OrderStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class OrderStatisticsController {
    private final OrderStatisticsService orderStatisticsService;

    @GetMapping("/orders")
    public Result<Map<String, Object>> getOrderStatistics(
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        return Result.success(orderStatisticsService.getOrderStatistics(startTime, endTime));
    }

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardStatistics() {
        return Result.success(orderStatisticsService.getDashboardStatistics());
    }
}
