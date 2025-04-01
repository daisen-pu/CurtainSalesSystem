package com.tencent.wxcloudrun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("material_stock_records")
public class MaterialStockRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer materialId;
    private String type;
    private BigDecimal quantity;
    private BigDecimal beforeStock;
    private BigDecimal afterStock;
    private String remark;
    private Long operatorId;
    private LocalDateTime createdAt;
}
