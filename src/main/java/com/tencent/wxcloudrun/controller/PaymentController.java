package com.tencent.wxcloudrun.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.common.Result;
import com.tencent.wxcloudrun.common.utils.SecurityUtils;
import com.tencent.wxcloudrun.entity.PaymentRecord;
import com.tencent.wxcloudrun.service.AliPayService;
import com.tencent.wxcloudrun.service.PaymentService;
import com.tencent.wxcloudrun.service.WxPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final WxPayService wxPayService;
    private final AliPayService aliPayService;

    @GetMapping("/order/{orderId}")
    public Result<IPage<PaymentRecord>> queryPaymentRecords(
        @PathVariable Long orderId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return Result.success(paymentService.queryPaymentRecords(orderId, page, size));
    }

    @GetMapping("/order/{orderId}/list")
    public Result<List<PaymentRecord>> getPaymentRecordsByOrderId(@PathVariable int orderId) {
        return Result.success(paymentService.getPaymentRecordsByOrderId(orderId));
    }

    @PostMapping
    public Result<PaymentRecord> createPaymentRecord(@RequestBody PaymentRecord paymentRecord) {
        paymentRecord.setOperatorId(SecurityUtils.getCurrentUserId());
        return Result.success(paymentService.createPaymentRecord(paymentRecord));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updatePaymentStatus(
        @PathVariable Long id,
        @RequestParam String status
    ) {
        paymentService.updatePaymentStatus(id, status);
        return Result.success();
    }

    /**
     * 创建微信支付订单
     */
    @PostMapping("/wxpay/create")
    public Result<Map<String, String>> createWxPayOrder(
        @RequestParam int orderId,
        @RequestParam BigDecimal amount,
        @RequestParam(defaultValue = "deposit") String type
    ) {
        return Result.success(wxPayService.createWxPayOrder(orderId, amount, type));
    }

    /**
     * 微信支付回调通知
     */
    @PostMapping("/wxpay/notify")
    public String handleWxPayNotify(@RequestBody Map<String, String> notifyData) {
        try {
            wxPayService.handleWxPayNotify(notifyData);
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        } catch (Exception e) {
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[" + e.getMessage() + "]]></return_msg></xml>";
        }
    }

    /**
     * 创建支付宝支付订单
     */
    @PostMapping("/alipay/create")
    public Result<String> createAliPayOrder(
        @RequestParam int orderId,
        @RequestParam BigDecimal amount,
        @RequestParam(defaultValue = "deposit") String type
    ) {
        return Result.success(aliPayService.createAliPayOrder(orderId, amount, type));
    }

    /**
     * 支付宝支付回调通知
     */
    @PostMapping("/alipay/notify")
    public String handleAliPayNotify(@RequestParam Map<String, String> params) {
        try {
            aliPayService.handleAliPayNotify(params);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }
}
