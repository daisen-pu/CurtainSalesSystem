package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.common.utils.SecurityUtils;
import com.tencent.wxcloudrun.entity.Order;
import com.tencent.wxcloudrun.entity.PaymentRecord;
import com.tencent.wxcloudrun.mapper.OrderMapper;
import com.tencent.wxcloudrun.mapper.PaymentRecordMapper;
import com.tencent.wxcloudrun.service.OrderStatusLogService;
import com.tencent.wxcloudrun.service.PaymentService;
import com.tencent.wxcloudrun.utils.PaymentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRecordMapper paymentRecordMapper;
    private final OrderMapper orderMapper;
    private final OrderStatusLogService orderStatusLogService;

    @Override
    public IPage<PaymentRecord> queryPaymentRecords(Long orderId, int page, int size) {
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentRecord::getOrderId, orderId)
            .orderByDesc(PaymentRecord::getCreatedAt);
        
        return paymentRecordMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public PaymentRecord getPaymentRecordById(Long id) {
        PaymentRecord record = paymentRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }
        return record;
    }

    @Override
    public List<PaymentRecord> getPaymentRecordsByOrderId(int orderId) {
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentRecord::getOrderId, orderId)
            .eq(PaymentRecord::getStatus, "success")
            .orderByDesc(PaymentRecord::getCreatedAt);
        
        return paymentRecordMapper.selectList(wrapper);
    }

    @Override
    public BigDecimal getTotalPaidAmount(int orderId) {
        List<PaymentRecord> records = getPaymentRecordsByOrderId(orderId);
        return records.stream()
            .map(PaymentRecord::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentRecord createPaymentRecord(PaymentRecord paymentRecord) {
        // 验证订单是否存在
        Order order = orderMapper.selectById(paymentRecord.getOrderId());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证支付金额
        PaymentUtils.validatePaymentAmount(
            paymentRecord.getOrderId(),
            paymentRecord.getAmount(),
            order.getTotalAmount(),
            paymentRecord.getType()
        );

        // 生成金额签名
        paymentRecord.setAmountSign(PaymentUtils.generateAmountSign(
            paymentRecord.getOrderId(),
            paymentRecord.getAmount()
        ));

        // 设置过期时间
        paymentRecord.setExpiredAt(PaymentUtils.getPaymentExpireTime());

        // 设置初始状态
        paymentRecord.setStatus("pending");
        paymentRecord.setCreatedAt(LocalDateTime.now());
        paymentRecord.setUpdatedAt(LocalDateTime.now());
        
        paymentRecordMapper.insert(paymentRecord);
        return paymentRecord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePaymentStatus(Long id, String status) {
        PaymentRecord record = getPaymentRecordById(id);
        
        // 检查支付是否已过期
        if (PaymentUtils.isPaymentExpired(record.getExpiredAt())) {
            record.setStatus("expired");
            record.setRemark("支付已过期");
            paymentRecordMapper.updateById(record);
            throw new BusinessException("支付已过期");
        }

        // 验证金额签名
        if (!PaymentUtils.verifyAmountSign(record.getOrderId(), record.getAmount(), record.getAmountSign())) {
            throw new BusinessException("支付金额验证失败");
        }
        
        // 检查状态变更是否合法
        if (!isValidStatusTransition(record.getStatus(), status)) {
            throw new BusinessException("非法的状态变更");
        }

        record.setStatus(status);
        record.setUpdatedAt(LocalDateTime.now());
        
        paymentRecordMapper.updateById(record);

        // 如果支付成功，更新订单的支付状态
        if ("success".equals(status)) {
            updateOrderPaymentStatus(record.getOrderId());
        }
    }

    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus == null) {
            return true;
        }
        
        switch (currentStatus) {
            case "pending":
                return "success".equals(newStatus) || "failed".equals(newStatus);
            case "success":
            case "failed":
                return false;
            default:
                return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateOrderPaymentStatus(int orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        BigDecimal totalAmount = order.getTotalAmount();
        BigDecimal paidAmount = getTotalPaidAmount(orderId);
        String oldPaymentStatus = order.getPaymentStatus();
        String newPaymentStatus;

        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            newPaymentStatus = "unpaid";
        } else if (paidAmount.compareTo(totalAmount) >= 0) {
            newPaymentStatus = "paid";
        } else {
            newPaymentStatus = "partial";
        }

        // 更新订单支付状态和已支付金额
        order.setPaymentStatus(newPaymentStatus);
        order.setPaidAmount(paidAmount);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        // 记录支付状态变更日志
        orderStatusLogService.logOrderStatusAndPaymentChange(
            orderId,
            order.getStatus(),
            order.getStatus(),
            oldPaymentStatus,
            newPaymentStatus,
            SecurityUtils.getCurrentUserId(),
            String.format("支付状态从 %s 变更为 %s，已支付金额：%.2f", oldPaymentStatus, newPaymentStatus, paidAmount)
        );
    }
}
