package com.tencent.wxcloudrun.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencent.wxcloudrun.common.Result;
import com.tencent.wxcloudrun.common.utils.SecurityUtils;
import com.tencent.wxcloudrun.entity.UserAddress;
import com.tencent.wxcloudrun.service.UserAddressService;
import com.tencent.wxcloudrun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class UserAddressController {
    
    @Autowired
    private UserAddressService userAddressService;
    
    @GetMapping("/list")
    public List<UserAddress> list(@RequestParam Long userId) {
        return userAddressService.list(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .orderByDesc(UserAddress::getIsDefault)
                .orderByDesc(UserAddress::getUpdatedAt));
    }
    
    @PostMapping
    public Result<Boolean> save(@RequestBody UserAddress address) {
        address.setUserId(address.getUserId());
        if(userAddressService.save(address)) {

            // 如果是默认地址，需要更新其他地址为非默认
            if (Boolean.TRUE.equals(address.getIsDefault())) {
                userAddressService.setDefault(address.getId(), address.getUserId());
            }
            return Result.success();
        }
        return Result.error("save failed");
    }
    
    @PutMapping
    public Result<Boolean> update(@RequestBody UserAddress address) {
        address.setUserId(address.getUserId());
        if(userAddressService.updateById(address)) {

            // 如果是默认地址，需要更新其他地址为非默认
            if (Boolean.TRUE.equals(address.getIsDefault())) {
                userAddressService.setDefault(address.getId(), address.getUserId());
            }
            return Result.success();
        }
        return Result.error("update failed");
    }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        if(userAddressService.removeById(id)){
            return Result.success();
        }
        return Result.error("delete failed");
    }


    @GetMapping("/{id}")
    public Result<UserAddress> get(@PathVariable Integer id) {
        return Result.success(userAddressService.getById(id));
    }
    
    @PutMapping("/default/{id}")
    public Result<Boolean> setDefault(@PathVariable Integer id) {
        // 先根据地址id查出用户ID
        UserAddress userAddress = userAddressService.getById(id);
        if (userAddress == null){
            return Result.error("系统异常：未能获取到正确的地址数据");
        }

        userAddressService.setDefault(id, userAddress.getUserId());
        return Result.success();
    }
}
