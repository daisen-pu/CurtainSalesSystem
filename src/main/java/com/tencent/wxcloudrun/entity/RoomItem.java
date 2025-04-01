package com.tencent.wxcloudrun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("room_items")
public class RoomItem {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer roomId;
    private Integer materialId;
    private String model;  // 新增型号字段
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
