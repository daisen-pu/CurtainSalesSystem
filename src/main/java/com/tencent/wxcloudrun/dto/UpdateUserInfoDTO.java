package com.tencent.wxcloudrun.dto;

import lombok.Data;
import javax.validation.constraints.Size;

@Data
public class UpdateUserInfoDTO {
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Size(max = 255, message = "头像URL长度不能超过255个字符")
    private String avatarUrl;
}
