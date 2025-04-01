package com.tencent.wxcloudrun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("order_room")
public class OrderRoom {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private String roomName;
    private BigDecimal windowWidth;
    private BigDecimal windowHeight;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @TableField(exist = false)
    private List<RoomItem> items;
    @TableField(exist = false)
    private List<RoomAttachment> attachments;
}
