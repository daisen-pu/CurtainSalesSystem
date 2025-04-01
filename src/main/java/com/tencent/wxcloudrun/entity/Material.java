package com.tencent.wxcloudrun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("materials")
public class Material {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer supplierId;
    private String name;
    private String code;
    private String description;
    private BigDecimal price;
    private String unit;
    private BigDecimal stock;
    private BigDecimal minStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
