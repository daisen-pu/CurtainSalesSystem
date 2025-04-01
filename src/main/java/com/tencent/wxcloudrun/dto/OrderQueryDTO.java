package com.tencent.wxcloudrun.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderQueryDTO {
    private Integer page = 1;
    private Integer size = 10;
    private String status;
    private Long userId;
    private String orderNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
