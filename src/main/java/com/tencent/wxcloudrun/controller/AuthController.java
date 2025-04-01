package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.common.Result;
import com.tencent.wxcloudrun.common.utils.JwtUtil;
import com.tencent.wxcloudrun.dto.UpdateUserInfoDTO;
import com.tencent.wxcloudrun.dto.WxLoginDTO;
import com.tencent.wxcloudrun.dto.WxLoginInfo;
import com.tencent.wxcloudrun.entity.User;
import com.tencent.wxcloudrun.service.UserService;
import com.tencent.wxcloudrun.service.WxService;
import lombok.RequiredArgsConstructor;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final WxService wxService;
    private final JwtUtil jwtUtil;

    @GetMapping("/info")
    public Result<User> getUserInfo() {
        User user = userService.getCurrentUser();
        if (user == null) {
            return Result.error("用户未登录");
        }

        return Result.success(user);
    }

    @PutMapping("/update-info")
    public Result<Void> updateUserInfo(@Valid @RequestBody UpdateUserInfoDTO updateUserInfoDTO) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return Result.error("用户未登录");
        }

        try {
            User updateUser = new User();
            updateUser.setId(currentUser.getId());
            updateUser.setNickname(updateUserInfoDTO.getNickname());
            updateUser.setAvatarUrl(updateUserInfoDTO.getAvatarUrl().replaceAll("uploads/",""));
            
            userService.updateUser(updateUser);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/wx/login")
    public Result<String> wxLogin(@Valid @RequestBody WxLoginDTO wxLoginDTO) {
        // 获取手机号和openid
        WxLoginInfo loginInfo = wxService.getPhoneNumberAndOpenid(
            wxLoginDTO.getCode(), 
            wxLoginDTO.getPhoneCode(),
            wxLoginDTO.getEncryptedData(), 
            wxLoginDTO.getIv()
        );
        
        if (loginInfo == null || loginInfo.getPhone() == null) {
            return Result.error("获取用户信息失败");
        }

        // 根据手机号和openid查找或创建用户
        User user = userService.loginOrRegisterByPhoneAndOpenid(loginInfo.getPhone(), loginInfo.getOpenid());
        
        // 生成 token
        String token = jwtUtil.generateToken(user.getUsername());
        return Result.success(token);
    }
}
