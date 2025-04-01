package com.tencent.wxcloudrun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order_status_logs")
public class OrderStatusLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private int orderId;
    private String oldStatus;
    private String newStatus;
    private String oldPaymentStatus;
    private String newPaymentStatus;
    private Long operatorId;
    private String remark;
    private LocalDateTime createdAt;
}
