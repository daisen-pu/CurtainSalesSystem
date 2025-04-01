package com.tencent.wxcloudrun.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付宝支付服务接口
 */
public interface AliPayService {
    /**
     * 创建支付宝支付订单
     * @param orderId 订单ID
     * @param amount 支付金额
     * @param type 支付类型（deposit/final）
     * @return 支付表单HTML
     */
    String createAliPayOrder(int orderId, BigDecimal amount, String type);

    /**
     * 处理支付宝支付回调通知
     * @param params 通知参数
     */
    void handleAliPayNotify(Map<String, String> params);
}
