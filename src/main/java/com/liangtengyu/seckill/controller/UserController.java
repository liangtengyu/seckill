package com.liangtengyu.seckill.controller;


import com.liangtengyu.seckill.common.authentication.AnonUrlsAndTimeout;
import com.liangtengyu.seckill.common.authentication.JWTToken;
import com.liangtengyu.seckill.common.authentication.JWTUtil;
import com.liangtengyu.seckill.common.config.mq.MQSender;
import com.liangtengyu.seckill.common.exception.SeckillException;
import com.liangtengyu.seckill.entity.User;
import com.liangtengyu.seckill.service.IUserService;
import com.liangtengyu.seckill.service.RedisService;
import com.liangtengyu.seckill.utils.MD5Util;
import com.liangtengyu.seckill.utils.Result;
import com.liangtengyu.seckill.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lty
 * @since 2021-02-05
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    RedisService redisService;

    //登录
    @RequestMapping("login")
    public Result login(String username, String password) {
        username = username.toLowerCase();
        String enPassword = MD5Util.encrypt(username,password);
        System.out.println(enPassword);
        //查找用户
        User user = userService.selectUserByName(username);
        //对比密码
        if (user == null || !enPassword.equals(user.getPassword())) {
            throw new SeckillException("用户名或密码错误");
        }
        //验证通过
        JWTToken jwtToken = JWTUtil.getJwtToken(username, enPassword);
        //将token存入redis
        redisService.set(jwtToken.getToken(), jwtToken.getToken(), AnonUrlsAndTimeout.timeout * 1000);
        return ResultUtil.requestSuccess(jwtToken);
    }

    //注册
    @RequestMapping("register")
    public Result register(String username, String password) {
        return null;
    }


    //测试队列发送和接收
    @Autowired
    MQSender mqSender;
    @RequestMapping("send")
    public void sendMqMessage() {
        HashMap<String,Object> m = new HashMap();
        m.put("username", "username");
        m.put("goodsId", "goodsId.toString()");

        mqSender.sendSeckillMessageToTestQueue("m.toString()");
    }


}
