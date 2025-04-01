package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencent.wxcloudrun.common.utils.SecurityUtils;
import com.tencent.wxcloudrun.entity.User;
import com.tencent.wxcloudrun.mapper.UserMapper;
import com.tencent.wxcloudrun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getCurrentUser() {
        return SecurityUtils.getCurrentUser();
    }

    @Override
    public User loginOrRegisterByPhoneAndOpenid(String phone, String openid) {
        // 先通过手机号查找用户
        User user = userMapper.findByPhone(phone);
        
        if (user != null) {
            // 如果用户存在，更新openid
            if (user.getOpenid() == null || !user.getOpenid().equals(openid)) {
                user.setOpenid(openid);
                userMapper.updateById(user);
            }
            return user;
        }
        
        // 如果用户不存在，通过openid查找
        user = userMapper.findByOpenid(openid);
        if (user != null) {
            // 如果通过openid找到用户，更新手机号
            user.setPhone(phone);
            userMapper.updateById(user);
            return user;
        }
        
        // 如果用户完全不存在，创建新用户
        user = new User();
        user.setPhone(phone);
        user.setOpenid(openid);
        user.setUsername(phone); // 默认使用手机号作为用户名
        user.setNickname("微信用户");
        
        // 检查用户名是否已存在，如果存在则添加随机后缀
        String baseUsername = phone;
        int suffix = 1;
        while (isUsernameExists(user.getUsername())) {
            user.setUsername(baseUsername + suffix);
            suffix++;
        }
        
        // 设置其他默认值
        user.setPassword(passwordEncoder.encode(openid)); // 使用openid作为初始密码
        user.setRole("user");
        user.setStatus("active");
        
        // 保存用户
        userMapper.insert(user);
        
        return user;
    }

    @Override
    public User getUserOperatorById(Long operatorId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, operatorId);
        return userMapper.selectOne(wrapper);
    }

    public User getUserByPhone(String phone){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        return userMapper.selectOne(wrapper);
    }

    public User getUserById(Long userId){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId);
        return userMapper.selectOne(wrapper);
    }

    /**
     * 检查用户名是否已存在
     */
    public boolean isUsernameExists(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userMapper.selectCount(wrapper) > 0;
    }

    @Override
    public User createUser(User user) {
        // 检查用户名是否已存在，如果存在则添加随机后缀
        String baseUsername = user.getPhone();
        if(StringUtils.isEmpty(user.getUsername())){
            user.setUsername(baseUsername);
        }
        int suffix = 1;
        while (isUsernameExists(user.getUsername())) {
            user.setUsername(baseUsername + suffix);
            suffix++;
        }

        // 设置其他默认值
        if(StringUtils.isNotEmpty(user.getOpenid())) {
            user.setPassword(passwordEncoder.encode(user.getOpenid()));
        }else {
            user.setPassword(passwordEncoder.encode("123456")); // 使用123456作为初始密码
        }
        if(StringUtils.isEmpty(user.getRole())) {
            user.setRole("USER");
        }
        user.setStatus("active");
        
        // 5. 设置时间戳
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        
        // 6. 插入数据
        if(userMapper.insert(user)>0) {
            return getUserByPhone(user.getPhone());
        }else {
            return null;
        }
    }

    @Override
    public User updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("用户信息不完整");
        }

        // 只更新非空的昵称和头像
        User updateUser = new User();
        updateUser.setId(user.getId());

        // 只有在传入值不为空时才更新
        if (StringUtils.isNotBlank(user.getPhone())) {
            updateUser.setPhone(user.getPhone());
        }
        if (StringUtils.isNotBlank(user.getNickname())) {
            updateUser.setNickname(user.getNickname());
        }
        if (StringUtils.isNotBlank(user.getAvatarUrl())) {
            updateUser.setAvatarUrl(user.getAvatarUrl());
        }
        updateUser.setUpdatedAt(LocalDateTime.now());

        userMapper.updateById(updateUser);
        SecurityUtils.updateCurrentUser(user.getId());
        return userMapper.selectById(user.getId());
    }

    @Override
    public List<User> searchUsers(String keyword) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(User::getRole, "ADMIN");
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                            .like(User::getUsername, keyword).or()
                            .like(User::getPhone, keyword)).or()
                    .like(User::getNickname, keyword).last("limit 10");
        }
        List<User> users = userMapper.selectList(queryWrapper);
        
        // 过滤敏感信息
        users.forEach(user -> {
            user.setPassword(null);
            user.setOpenid(null);
            // 可以根据需要过滤其他敏感字段
        });
        
        return users;
    }
}
