package com.tencent.wxcloudrun.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MaterialStockQueryDTO extends PageDTO {
    private Integer materialId;
    private String type;
}
