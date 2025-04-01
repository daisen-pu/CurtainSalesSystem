package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.entity.Order;
import com.tencent.wxcloudrun.entity.PaymentRecord;
import com.tencent.wxcloudrun.entity.User;
import com.tencent.wxcloudrun.mapper.OrderMapper;
import com.tencent.wxcloudrun.mapper.UserMapper;
import com.tencent.wxcloudrun.service.PaymentConfigService;
import com.tencent.wxcloudrun.service.PaymentService;
import com.tencent.wxcloudrun.service.WxPayService;
import com.tencent.wxcloudrun.utils.PaymentUtils;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WxPayServiceImpl implements WxPayService {
    private final PaymentConfigService paymentConfigService;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> createWxPayOrder(int orderId, BigDecimal amount, String type) {
        // 验证订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 获取用户openid
        User user = userMapper.selectById(order.getUserId());
        if (user == null || user.getOpenid() == null || user.getOpenid().isEmpty()) {
            throw new BusinessException("用户未绑定微信，无法使用微信支付");
        }

        // 验证支付金额
        PaymentUtils.validatePaymentAmount(orderId, amount, order.getTotalAmount(), type);

        // 创建支付记录
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setOrderId(orderId);
        paymentRecord.setAmount(amount);
        paymentRecord.setPaymentMethod("wechat");
        paymentRecord.setType(type);
        paymentService.createPaymentRecord(paymentRecord);

        try {
            // 获取微信支付配置
            Map<String, String> wxConfig = paymentConfigService.getPaymentConfig("WXPAY");
            WXPay wxPay = new WXPay(new WXPayConfig() {
                @Override
                public String getAppID() {
                    return wxConfig.get("appId");
                }

                @Override
                public String getMchID() {
                    return wxConfig.get("mchId");
                }

                @Override
                public String getKey() {
                    return wxConfig.get("key");
                }

                @Override
                public InputStream getCertStream() {
                    try {
                        String certPath = wxConfig.get("certPath");
                        return new FileInputStream(new File(certPath));
                    } catch (Exception e) {
                        throw new BusinessException("获取证书失败");
                    }
                }

                @Override
                public int getHttpConnectTimeoutMs() {
                    return 30;
                }

                @Override
                public int getHttpReadTimeoutMs() {
                    return 30;
                }
            });

            // 准备支付参数
            Map<String, String> data = new HashMap<>();
            data.put("body", String.format("窗帘订单-%s支付", "deposit".equals(type) ? "定金" : "尾款"));
            data.put("out_trade_no", paymentRecord.getId().toString());
            data.put("total_fee", amount.multiply(new BigDecimal("100")).intValue() + "");
            data.put("spbill_create_ip", "127.0.0.1");
            data.put("notify_url", wxConfig.get("notifyUrl"));
            data.put("trade_type", "JSAPI");
            data.put("openid", user.getOpenid());

            // 添加过期时间
            data.put("time_expire", LocalDateTime.now().plus(30, ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

            // 调用统一下单接口
            Map<String, String> response = wxPay.unifiedOrder(data);

            if ("SUCCESS".equals(response.get("return_code")) && "SUCCESS".equals(response.get("result_code"))) {
                // 生成支付参数
                Map<String, String> payParams = new HashMap<>();
                payParams.put("appId", wxConfig.get("appId"));
                payParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
                payParams.put("nonceStr", WXPayUtil.generateNonceStr());
                payParams.put("package", "prepay_id=" + response.get("prepay_id"));
                payParams.put("signType", WXPayConstants.MD5);

                // 计算签名
                String sign = WXPayUtil.generateSignature(payParams, wxConfig.get("key"), WXPayConstants.SignType.MD5);
                payParams.put("paySign", sign);

                return payParams;
            } else {
                throw new BusinessException("创建微信支付订单失败：" + response.get("err_code_des"));
            }
        } catch (Exception e) {
            throw new BusinessException("创建微信支付订单失败：" + e.getMessage());
        }
    }

    @Override
    public void handleWxPayNotify(Map<String, String> notifyData) {
        try {
            // 获取微信支付配置
            Map<String, String> wxConfig = paymentConfigService.getPaymentConfig("WXPAY");
            WXPay wxPay = new WXPay(new WXPayConfig() {
                @Override
                public String getAppID() {
                    return wxConfig.get("appId");
                }

                @Override
                public String getMchID() {
                    return wxConfig.get("mchId");
                }

                @Override
                public String getKey() {
                    return wxConfig.get("key");
                }

                @Override
                public InputStream getCertStream() {
                    try {
                        String certPath = wxConfig.get("certPath");
                        return new FileInputStream(new File(certPath));
                    } catch (Exception e) {
                        throw new BusinessException("获取证书失败");
                    }
                }

                @Override
                public int getHttpConnectTimeoutMs() {
                    return 30;
                }

                @Override
                public int getHttpReadTimeoutMs() {
                    return 30;
                }
            });

            // 验证签名
            if (!wxPay.isPayResultNotifySignatureValid(notifyData)) {
                throw new BusinessException("微信支付通知签名验证失败");
            }

            // 处理支付结果
            if ("SUCCESS".equals(notifyData.get("return_code")) && "SUCCESS".equals(notifyData.get("result_code"))) {
                String paymentId = notifyData.get("out_trade_no");
                String transactionId = notifyData.get("transaction_id");

                // 获取支付记录
                PaymentRecord record = paymentService.getPaymentRecordById(Long.parseLong(paymentId));

                // 检查支付是否已过期
                if (PaymentUtils.isPaymentExpired(record.getExpiredAt())) {
                    throw new BusinessException("支付已过期");
                }

                // 验证支付金额
                String totalFee = notifyData.get("total_fee");
                BigDecimal notifyAmount = new BigDecimal(totalFee).divide(new BigDecimal("100"));
                if (!PaymentUtils.verifyAmountSign(record.getOrderId(), notifyAmount, record.getAmountSign())) {
                    throw new BusinessException("支付金额验证失败");
                }

                // 更新支付记录状态
                record.setTransactionNo(transactionId);
                paymentService.updatePaymentStatus(record.getId(), "success");
            }
        } catch (Exception e) {
            throw new BusinessException("处理微信支付通知失败：" + e.getMessage());
        }
    }
}
