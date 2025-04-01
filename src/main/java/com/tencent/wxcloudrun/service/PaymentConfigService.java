package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.entity.PaymentConfig;
import java.util.Map;

public interface PaymentConfigService {
    /**
     * 获取支付配置
     * @param configType 配置类型
     * @return 配置Map
     */
    Map<String, String> getPaymentConfig(String configType);

    /**
     * 更新支付配置
     * @param paymentConfig 支付配置
     * @return 是否成功
     */
    boolean updatePaymentConfig(PaymentConfig paymentConfig);
}
