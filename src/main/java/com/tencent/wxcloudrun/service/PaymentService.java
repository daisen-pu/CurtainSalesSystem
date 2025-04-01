package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.entity.PaymentRecord;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    IPage<PaymentRecord> queryPaymentRecords(Long orderId, int page, int size);
    PaymentRecord getPaymentRecordById(Long id);
    List<PaymentRecord> getPaymentRecordsByOrderId(int orderId);
    BigDecimal getTotalPaidAmount(int orderId);
    PaymentRecord createPaymentRecord(PaymentRecord paymentRecord);
    void updatePaymentStatus(Long id, String status);
}
