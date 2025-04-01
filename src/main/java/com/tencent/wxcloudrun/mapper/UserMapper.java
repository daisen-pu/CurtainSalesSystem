package com.tencent.wxcloudrun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据openid查找用户
     */
    User findByOpenid(String openid);

    /**
     * 根据手机号查找用户
     */
    User findByPhone(String phone);

    /**
     * 根据ID查找用户
     */
    User findById(Integer id);

    /**
     * 插入用户
     */
    int insert(User user);

    /**
     * 更新用户
     */
    int updateById(User user);
}
