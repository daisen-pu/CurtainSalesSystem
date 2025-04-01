package com.tencent.wxcloudrun.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencent.wxcloudrun.entity.User;
import com.tencent.wxcloudrun.mapper.UserMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Arrays;

@Component
public class SecurityUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static final String CURRENT_USER_KEY = "CURRENT_USER";
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SecurityUtils.applicationContext = applicationContext;
    }
    
    public static User getCurrentUser() {
        // 1. 先从请求上下文中获取缓存的用户信息
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            User cachedUser = (User) attributes.getAttribute(CURRENT_USER_KEY, RequestAttributes.SCOPE_REQUEST);
            if (cachedUser != null) {
                return cachedUser;
            }
        }

        // 2. 如果没有缓存，则从数据库查询
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            String phone = (String) authentication.getPrincipal();
            UserMapper userMapper = applicationContext.getBean(UserMapper.class);
            User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, phone)
            );

            // 3. 将查询结果缓存到请求上下文中
            if (user != null && attributes != null) {
                attributes.setAttribute(CURRENT_USER_KEY, user, RequestAttributes.SCOPE_REQUEST);
            }
            return user;
        }
        return null;
    }

    /**
     * 当修改用户信息之后刷新缓存
     * @param userId
     * @return
     */
    public static User updateCurrentUser(Long userId){
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getId, userId)
        );

        // 3. 将查询结果缓存到请求上下文中
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (user != null && attributes != null) {
            attributes.setAttribute(CURRENT_USER_KEY, user, RequestAttributes.SCOPE_REQUEST);
        }
        return user;
    }

    public static Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public static String getCurrentUsername() {
        User user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }

    /**
     * 判断用户是否具有admin权限
     * @return
     */
    public static boolean isAdmin() {
        User user = getCurrentUser();
        return user != null && "ADMIN".equals(user.getRole().toUpperCase());
    }

    /**
     * 判断用户是否具有User权限
     * @return
     */
    public static boolean isUser() {
        User user = getCurrentUser();
        return user != null && "USER".equals(user.getRole().toUpperCase());
    }
}
