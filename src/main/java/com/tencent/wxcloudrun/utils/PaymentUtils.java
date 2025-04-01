package com.tencent.wxcloudrun.utils;

import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.service.PaymentConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Component
public class PaymentUtils {
    private static PaymentConfigService paymentConfigService;
    private static final long PAYMENT_TIMEOUT_MINUTES = 30; // 支付超时时间（分钟）

    @Autowired
    public void setPaymentConfigService(PaymentConfigService paymentConfigService) {
        PaymentUtils.paymentConfigService = paymentConfigService;
    }

    private static String getAmountSignSecret() {
        try {
            Map<String, String> config = paymentConfigService.getPaymentConfig("PAYMENT");
            return config.get("amountSignSecret");
        } catch (Exception e) {
            throw new BusinessException("获取支付配置失败");
        }
    }

    /**
     * 生成金额签名
     * @param orderId 订单ID
     * @param amount 金额
     * @return 签名
     */
    public static String generateAmountSign(Integer orderId, BigDecimal amount) {
        String content = String.format("%d:%.2f:%s", orderId, amount, getAmountSignSecret());
        return DigestUtils.md5DigestAsHex(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 验证金额签名
     * @param orderId 订单ID
     * @param amount 金额
     * @param sign 签名
     * @return 是否有效
     */
    public static boolean verifyAmountSign(Integer orderId, BigDecimal amount, String sign) {
        String expectedSign = generateAmountSign(orderId, amount);
        return expectedSign.equals(sign);
    }

    /**
     * 获取支付过期时间
     * @return 过期时间
     */
    public static LocalDateTime getPaymentExpireTime() {
        return LocalDateTime.now().plus(PAYMENT_TIMEOUT_MINUTES, ChronoUnit.MINUTES);
    }

    /**
     * 检查支付是否已过期
     * @param expiredAt 过期时间
     * @return 是否已过期
     */
    public static boolean isPaymentExpired(LocalDateTime expiredAt) {
        return expiredAt != null && LocalDateTime.now().isAfter(expiredAt);
    }

    /**
     * 验证支付金额
     * @param orderId 订单ID
     * @param requestAmount 请求支付金额
     * @param orderAmount 订单实际金额
     * @param paymentType 支付类型（deposit/final）
     */
    public static void validatePaymentAmount(Integer orderId, BigDecimal requestAmount, 
                                           BigDecimal orderAmount, String paymentType) {
        if (requestAmount == null || requestAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("支付金额无效");
        }

        BigDecimal maxAmount;
        if ("deposit".equals(paymentType)) {
            // 定金最大为订单总额的50%
            maxAmount = orderAmount.multiply(new BigDecimal("0.5"));
        } else if ("final".equals(paymentType)) {
            // 尾款为订单总额减去已支付金额
            maxAmount = orderAmount;
        } else {
            throw new BusinessException("无效的支付类型");
        }

        if (requestAmount.compareTo(maxAmount) > 0) {
            throw new BusinessException("支付金额超过限制");
        }
    }
}
