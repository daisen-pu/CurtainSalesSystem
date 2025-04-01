package com.tencent.wxcloudrun.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderListDTO {
    private Integer id;
    private Long userId;
    private String nickName;
    private String orderNumber;
    private BigDecimal totalAmount;
    private String status;
    private String address;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
