package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class UserAddressDTO {
    private Integer id;
    private Integer userId;
    private String address;
    private Boolean isDefault;
}
