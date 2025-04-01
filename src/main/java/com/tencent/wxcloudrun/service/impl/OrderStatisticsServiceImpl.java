package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.common.utils.SecurityUtils;
import com.tencent.wxcloudrun.constant.OrderConstants.StatisticsKey;
import com.tencent.wxcloudrun.mapper.OrderMapper;
import com.tencent.wxcloudrun.service.OrderStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单统计服务实现类
 */
@Service
@RequiredArgsConstructor
public class OrderStatisticsServiceImpl implements OrderStatisticsService {
    private final OrderMapper orderMapper;

    /**
     * 获取订单统计数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 订单统计数据，包含：
     *         - totalOrders: 订单总数
     *         - pendingOrders: 待处理订单数
     *         - processingOrders: 处理中订单数
     *         - completedOrders: 已完成订单数
     *         - cancelledOrders: 已取消订单数
     *         - unpaidOrders: 未支付订单数
     *         - partialPaidOrders: 部分支付订单数
     *         - paidOrders: 已支付订单数
     *         - totalAmount: 订单总金额
     *         - totalPaidAmount: 已支付总金额
     */
    @Override
    public Map<String, Object> getOrderStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        // 如果不是管理员，只统计自己的订单
        Long userId = null;
        if (!SecurityUtils.isAdmin()) {
            userId = SecurityUtils.getCurrentUser().getId();
        }
        
        List<Map<String, Object>> statistics = orderMapper.getOrderStatistics(startTime, endTime, userId);
        if (statistics.isEmpty()) {
            Map<String, Object> defaultStats = new HashMap<>();
            defaultStats.put(StatisticsKey.TOTAL_ORDERS, 0);
            defaultStats.put(StatisticsKey.PENDING_ORDERS, 0);
            defaultStats.put(StatisticsKey.PROCESSING_ORDERS, 0);
            defaultStats.put(StatisticsKey.COMPLETED_ORDERS, 0);
            defaultStats.put(StatisticsKey.CANCELLED_ORDERS, 0);
            defaultStats.put(StatisticsKey.UNPAID_ORDERS, 0);
            defaultStats.put(StatisticsKey.PARTIAL_PAID_ORDERS, 0);
            defaultStats.put(StatisticsKey.PAID_ORDERS, 0);
            defaultStats.put(StatisticsKey.TOTAL_AMOUNT, 0.0);
            defaultStats.put(StatisticsKey.TOTAL_PAID_AMOUNT, 0.0);
            return defaultStats;
        }
        return statistics.get(0);
    }

    /**
     * 获取用户订单统计数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户订单统计数据列表，每个用户包含：
     *         - userId: 用户ID
     *         - userName: 用户名称
     *         - totalOrders: 订单总数
     *         - totalAmount: 订单总金额
     *         - totalPaidAmount: 已支付总金额
     *         - lastOrderTime: 最后下单时间
     */
    @Override
    public List<Map<String, Object>> getUserOrderStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return orderMapper.getUserOrderStatistics(startTime, endTime);
    }

    /**
     * 获取仪表盘统计数据
     *
     * @return 仪表盘统计数据，包含：
     *         - today: 今日订单统计
     *         - month: 本月订单统计
     *         - total: 总体订单统计
     *         - userStats: 最近用户订单统计
     */
    @Override
    public Map<String, Object> getDashboardStatistics() {
        // 获取今日数据
        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.now().with(LocalTime.MAX);
        Map<String, Object> todayStats = getOrderStatistics(todayStart, todayEnd);

        // 获取本月数据
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime monthEnd = LocalDateTime.now().with(LocalTime.MAX);
        Map<String, Object> monthStats = getOrderStatistics(monthStart, monthEnd);

        // 获取总体数据
        Map<String, Object> totalStats = getOrderStatistics(null, null);

        // 合并统计数据
        Map<String, Object> result = new HashMap<>();
        result.put(StatisticsKey.TODAY, todayStats);
        result.put(StatisticsKey.MONTH, monthStats);
        result.put(StatisticsKey.TOTAL, totalStats);

        // 获取最近的用户订单统计
        List<Map<String, Object>> userStats = getUserOrderStatistics(monthStart, monthEnd);
        result.put(StatisticsKey.USER_STATS, userStats);

        return result;
    }
}
