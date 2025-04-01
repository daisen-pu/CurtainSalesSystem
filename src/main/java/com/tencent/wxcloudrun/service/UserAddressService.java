package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tencent.wxcloudrun.entity.UserAddress;

public interface UserAddressService extends IService<UserAddress> {
    // 设置默认地址
    void setDefault(Integer addressId, Long userId);
}
