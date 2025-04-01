package com.tencent.wxcloudrun.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class WxCodeLoginDTO {
    @NotBlank(message = "code不能为空")
    private String code;
}
