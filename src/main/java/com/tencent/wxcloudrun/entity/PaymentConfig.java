package com.tencent.wxcloudrun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("payment_config")
public class PaymentConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String configType;
    private String configKey;
    private String configValue;
    private int status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;
}
