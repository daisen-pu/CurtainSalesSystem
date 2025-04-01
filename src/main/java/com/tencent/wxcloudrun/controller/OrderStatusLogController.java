package com.tencent.wxcloudrun.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.common.Result;
import com.tencent.wxcloudrun.entity.OrderStatusLog;
import com.tencent.wxcloudrun.service.OrderStatusLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-status-logs")
@RequiredArgsConstructor
public class OrderStatusLogController {
    private final OrderStatusLogService orderStatusLogService;

    @GetMapping("/order/{orderId}")
    public Result<IPage<OrderStatusLog>> queryOrderStatusLogs(
        @PathVariable Long orderId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return Result.success(orderStatusLogService.queryOrderStatusLogs(orderId, page, size));
    }
}
