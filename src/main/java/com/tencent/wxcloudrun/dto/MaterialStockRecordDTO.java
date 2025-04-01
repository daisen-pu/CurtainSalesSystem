package com.tencent.wxcloudrun.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaterialStockRecordDTO {
    private Integer id;
    private Integer materialId;
    private String materialName;
    private String type;
    private BigDecimal quantity;
    private BigDecimal beforeStock;
    private BigDecimal afterStock;
    private String remark;
    private Long operatorId;
    private String operatorName;
    private LocalDateTime createdAt;
}
