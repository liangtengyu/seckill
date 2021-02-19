package com.liangtengyu.seckill.mapper;

import com.liangtengyu.seckill.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lty
 * @since 2021-02-05
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    User selectByname(String username);
}
