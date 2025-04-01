package com.tencent.wxcloudrun.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 微信支付服务接口
 */
public interface WxPayService {
    /**
     * 创建微信支付订单
     * @param orderId 订单ID
     * @param amount 支付金额
     * @param type 支付类型（deposit/final）
     * @return 支付参数
     */
    Map<String, String> createWxPayOrder(int orderId, BigDecimal amount, String type);

    /**
     * 处理微信支付回调通知
     * @param notifyData 通知数据
     */
    void handleWxPayNotify(Map<String, String> notifyData);
}
