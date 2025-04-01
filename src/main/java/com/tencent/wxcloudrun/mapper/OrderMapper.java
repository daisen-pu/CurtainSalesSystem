package com.tencent.wxcloudrun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.dto.OrderQueryDTO;
import com.tencent.wxcloudrun.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    IPage<Order> queryOrders(IPage<Order> page, @Param("query") OrderQueryDTO query);
    
    /**
     * 获取订单统计数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param creatorId 创建者ID（可选，为null时查询所有用户的订单）
     * @return 订单统计数据
     */
    List<Map<String, Object>> getOrderStatistics(
        @Param("startTime") LocalDateTime startTime, 
        @Param("endTime") LocalDateTime endTime,
        @Param("creatorId") Long creatorId
    );

    /**
     * 获取用户订单统计数据（按创建者分组统计）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户订单统计数据，包含：
     *         - creatorId: 创建者ID
     *         - creatorName: 创建者名称
     *         - totalOrders: 订单总数
     *         - totalAmount: 订单总金额
     *         - totalPaidAmount: 已支付总金额
     *         - lastOrderTime: 最后下单时间
     */
    List<Map<String, Object>> getUserOrderStatistics(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
