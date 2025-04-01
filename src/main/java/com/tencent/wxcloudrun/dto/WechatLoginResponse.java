package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class WechatLoginResponse {
    private String openid;
    private String session_key;
    private String unionid;
    private String errcode;
    private String errmsg;
}
