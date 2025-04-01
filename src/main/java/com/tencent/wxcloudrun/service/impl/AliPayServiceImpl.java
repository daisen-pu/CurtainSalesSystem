package com.tencent.wxcloudrun.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.entity.Order;
import com.tencent.wxcloudrun.entity.PaymentRecord;
import com.tencent.wxcloudrun.mapper.OrderMapper;
import com.tencent.wxcloudrun.service.AliPayService;
import com.tencent.wxcloudrun.service.PaymentConfigService;
import com.tencent.wxcloudrun.service.PaymentService;
import com.tencent.wxcloudrun.utils.PaymentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AliPayServiceImpl implements AliPayService {
    private final PaymentConfigService paymentConfigService;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createAliPayOrder(int orderId, BigDecimal amount, String type) {
        // 验证订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证支付金额
        PaymentUtils.validatePaymentAmount(orderId, amount, order.getTotalAmount(), type);

        // 创建支付记录
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setOrderId(orderId);
        paymentRecord.setAmount(amount);
        paymentRecord.setPaymentMethod("alipay");
        paymentRecord.setType(type);
        paymentService.createPaymentRecord(paymentRecord);

        try {
            // 获取支付宝配置
            Map<String, String> alipayConfig = paymentConfigService.getPaymentConfig("ALIPAY");
            
            // 创建AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                alipayConfig.get("appId"),
                alipayConfig.get("privateKey"),
                "json",
                "UTF-8",
                alipayConfig.get("publicKey"),
                "RSA2"
            );

            // 创建API对应的request
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setReturnUrl(alipayConfig.get("returnUrl"));
            request.setNotifyUrl(alipayConfig.get("notifyUrl"));

            // 计算过期时间
            LocalDateTime expireTime = PaymentUtils.getPaymentExpireTime();
            String timeExpire = expireTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // 构建请求参数
            String bizContent = String.format(
                "{" +
                "    \"out_trade_no\":\"%s\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":%.2f," +
                "    \"subject\":\"%s\"," +
                "    \"body\":\"订单号：%d\"," +
                "    \"time_expire\":\"%s\"" +
                "}",
                paymentRecord.getId(),
                amount,
                String.format("窗帘订单-%s支付", "deposit".equals(type) ? "定金" : "尾款"),
                orderId,
                timeExpire
            );
            request.setBizContent(bizContent);

            // 调用SDK生成表单
            return alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            throw new BusinessException("创建支付宝支付订单失败：" + e.getMessage());
        }
    }

    @Override
    public void handleAliPayNotify(Map<String, String> params) {
        try {
            // 获取支付宝配置
            Map<String, String> alipayConfig = paymentConfigService.getPaymentConfig("ALIPAY");

            // 验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(
                params,
                alipayConfig.get("publicKey"),
                "UTF-8",
                "RSA2"
            );

            if (!signVerified) {
                throw new BusinessException("支付宝支付通知签名验证失败");
            }

            // 处理支付结果
            if ("TRADE_SUCCESS".equals(params.get("trade_status"))) {
                String paymentId = params.get("out_trade_no");
                String transactionId = params.get("trade_no");
                
                // 获取支付记录
                PaymentRecord record = paymentService.getPaymentRecordById(Long.parseLong(paymentId));

                // 检查支付是否已过期
                if (PaymentUtils.isPaymentExpired(record.getExpiredAt())) {
                    throw new BusinessException("支付已过期");
                }

                // 验证支付金额
                BigDecimal notifyAmount = new BigDecimal(params.get("total_amount"));
                if (!PaymentUtils.verifyAmountSign(record.getOrderId(), notifyAmount, record.getAmountSign())) {
                    throw new BusinessException("支付金额验证失败");
                }
                
                // 更新支付记录状态
                record.setTransactionNo(transactionId);
                paymentService.updatePaymentStatus(record.getId(), "success");
            }
        } catch (AlipayApiException e) {
            throw new BusinessException("处理支付宝支付通知失败：" + e.getMessage());
        }
    }
}
