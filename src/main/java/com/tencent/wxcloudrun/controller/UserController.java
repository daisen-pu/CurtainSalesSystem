package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.common.Result;
import com.tencent.wxcloudrun.entity.User;
import com.tencent.wxcloudrun.service.UserService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/search")
    public List<User> search(@RequestParam String keyword) {
        // 返回的用户信息应该过滤掉敏感字段（如密码）
        return userService.searchUsers(keyword);
    }
    
    @PostMapping
    public Result<User> createUser(@RequestBody User user) {
        if (StringUtils.isEmpty(user.getPhone())) {
            return Result.error("手机号不能为空");
        }
        // 检查手机号是否已存在
        if (userService.getUserByPhone(user.getPhone()) != null) {
            return Result.error("手机号已存在，请不要重复创建");
        }
        
        return Result.success(userService.createUser(user));
    }
}
