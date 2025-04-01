package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.entity.OrderStatusLog;

public interface OrderStatusLogService {
    IPage<OrderStatusLog> queryOrderStatusLogs(Long orderId, int page, int size);
    
    OrderStatusLog createOrderStatusLog(OrderStatusLog log);
    
    void logOrderStatusChange(int orderId, String oldStatus, String newStatus, Long operatorId, String remark);
    
    void logOrderStatusAndPaymentChange(
        int orderId,
        String oldStatus,
        String newStatus,
        String oldPaymentStatus,
        String newPaymentStatus,
        Long operatorId,
        String remark
    );
}
