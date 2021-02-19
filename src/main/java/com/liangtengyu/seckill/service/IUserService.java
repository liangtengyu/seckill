package com.liangtengyu.seckill.service;

import com.liangtengyu.seckill.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liangtengyu.seckill.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lty
 * @since 2021-02-05
 */
public interface IUserService extends IService<User> {

    User selectUserByName(String username);


}
