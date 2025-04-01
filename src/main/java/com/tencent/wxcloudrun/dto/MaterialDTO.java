package com.tencent.wxcloudrun.dto;

import com.tencent.wxcloudrun.entity.Supplier;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MaterialDTO {
    private Integer id;
    
    @NotBlank(message = "材料名称不能为空")
    private String name;
    
    @NotBlank(message = "材料编码不能为空")
    private String code;
    
    private String description;
    
    @NotNull(message = "材料价格不能为空")
    @DecimalMin(value = "0.01", message = "材料价格必须大于0")
    private BigDecimal price;
    
    @NotBlank(message = "计量单位不能为空")
    private String unit;
    
    private BigDecimal stock;
    
    @NotNull(message = "最小库存不能为空")
    @DecimalMin(value = "0", message = "最小库存不能小于0")
    private BigDecimal minStock;

    private Supplier supplier;
    
    private Integer supplierId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<MaterialAttachmentDTO> attachments;
}
