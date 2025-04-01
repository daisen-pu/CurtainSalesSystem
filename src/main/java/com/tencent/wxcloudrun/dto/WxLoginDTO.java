package com.tencent.wxcloudrun.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WxLoginDTO {
    @NotBlank(message = "登录code不能为空")
    private String code;

    @NotBlank(message = "手机号code不能为空")
    private String phoneCode;

    private String encryptedData;

    private String iv;
}
