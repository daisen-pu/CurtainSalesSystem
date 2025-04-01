package com.tencent.wxcloudrun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("payment_records")
public class PaymentRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Integer orderId;
    
    private BigDecimal amount;
    
    private String paymentMethod;
    
    private String status;
    private String type;
    private String transactionNo;
    
    private Long operatorId;
    
    private String remark;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private LocalDateTime expiredAt; // 支付过期时间
    
    // 用于金额验证的签名
    private String amountSign;
}
