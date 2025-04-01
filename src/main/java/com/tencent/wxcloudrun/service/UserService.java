package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.entity.User;

import java.util.List;

public interface UserService {
    User getCurrentUser();
    
    default Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    User loginOrRegisterByPhoneAndOpenid(String phone, String openid);

    User getUserOperatorById(Long operatorId);

    User getUserByPhone(String phone);
    User getUserById(Long userId);

    // 检查用户名是否已存在
    boolean isUsernameExists(String username);
    
    User createUser(User user);

    /**
     * 更新用户信息
     * @param user 用户信息（只更新昵称和头像）
     * @return 更新后的用户信息
     */
    User updateUser(User user);
    
    /**
     * 搜索用户
     * @param keyword 关键字（用户名或手机号）
     * @return 匹配的用户列表（已过滤敏感信息）
     */
    List<User> searchUsers(String keyword);
}
