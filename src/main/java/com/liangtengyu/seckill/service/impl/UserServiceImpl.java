package com.liangtengyu.seckill.service.impl;

import com.liangtengyu.seckill.entity.User;
import com.liangtengyu.seckill.mapper.UserMapper;
import com.liangtengyu.seckill.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lty
 * @since 2021-02-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User selectUserByName(String username) {
        return userMapper.selectByname(username);
    }
}
