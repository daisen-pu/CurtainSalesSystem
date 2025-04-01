package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.entity.OrderStatusLog;
import com.tencent.wxcloudrun.mapper.OrderStatusLogMapper;
import com.tencent.wxcloudrun.service.OrderStatusLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderStatusLogServiceImpl implements OrderStatusLogService {
    private final OrderStatusLogMapper orderStatusLogMapper;

    @Override
    public IPage<OrderStatusLog> queryOrderStatusLogs(Long orderId, int page, int size) {
        LambdaQueryWrapper<OrderStatusLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderStatusLog::getOrderId, orderId)
            .orderByDesc(OrderStatusLog::getCreatedAt);
        
        return orderStatusLogMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderStatusLog createOrderStatusLog(OrderStatusLog log) {
        log.setCreatedAt(LocalDateTime.now());
        orderStatusLogMapper.insert(log);
        return log;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logOrderStatusChange(int orderId, String oldStatus, String newStatus, Long operatorId, String remark) {
        logOrderStatusAndPaymentChange(orderId, oldStatus, newStatus, null, null, operatorId, remark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logOrderStatusAndPaymentChange(
        int orderId,
        String oldStatus,
        String newStatus,
        String oldPaymentStatus,
        String newPaymentStatus,
        Long operatorId,
        String remark
    ) {
        OrderStatusLog log = new OrderStatusLog();
        log.setOrderId(orderId);
        log.setOldStatus(oldStatus);
        log.setNewStatus(newStatus);
        log.setOldPaymentStatus(oldPaymentStatus);
        log.setNewPaymentStatus(newPaymentStatus);
        log.setOperatorId(operatorId);
        log.setRemark(remark);
        
        createOrderStatusLog(log);
    }
}
