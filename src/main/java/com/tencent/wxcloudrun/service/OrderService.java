package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tencent.wxcloudrun.common.Result;
import com.tencent.wxcloudrun.dto.OrderDTO;
import com.tencent.wxcloudrun.dto.OrderListDTO;
import com.tencent.wxcloudrun.dto.OrderQueryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderService {
    /**
     * 查询订单列表（简化版，不包含关联信息）
     */
    IPage<OrderListDTO> queryOrderList(OrderQueryDTO query);
    OrderDTO getOrderDetail(int id);
    Result<OrderDTO> createOrder(OrderDTO orderDTO);
    void updateOrderStatus(int id, String status);
    void updatePaymentStatus(int id, String paymentStatus, BigDecimal paidAmount);
    void deleteOrder(int id);
    /**
     * 更新订单信息
     */
    @Transactional
    OrderDTO updateOrder(OrderDTO orderDTO);
}
