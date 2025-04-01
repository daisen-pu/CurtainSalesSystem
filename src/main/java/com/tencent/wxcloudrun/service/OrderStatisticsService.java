package com.tencent.wxcloudrun.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单统计服务接口
 */
public interface OrderStatisticsService {
    /**
     * 获取订单统计数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 订单统计数据
     */
    Map<String, Object> getOrderStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取用户订单统计数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户订单统计数据列表
     */
    List<Map<String, Object>> getUserOrderStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取仪表盘统计数据
     *
     * @return 仪表盘统计数据
     */
    Map<String, Object> getDashboardStatistics();
}
