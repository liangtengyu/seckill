package com.liangtengyu.seckill.common.authentication;


import com.liangtengyu.seckill.entity.User;
import com.liangtengyu.seckill.mapper.UserMapper;
import com.liangtengyu.seckill.service.RedisService;
import com.liangtengyu.seckill.utils.CommonUtil;
import com.liangtengyu.seckill.utils.HttpContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 *
 * @author lty
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }


    //授权模块 暂时没用到
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
        return null;
    }

    /**
     * 用户认证
     *
     * @param authenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 这里的 token是从 JWTFilter 的 executeLogin 方法传递过来的，已经经过了解密
        String token = (String) authenticationToken.getCredentials();

        // 从 redis里获取这个 token
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();

        String encryptToken = CommonUtil.encryptToken(token);
        String encryptTokenInRedis = null;
        try {
            encryptTokenInRedis = redisService.get(encryptToken);//可以通过encryptToken加入IP的方式,来处理单点登录
        } catch (Exception ignore) {
        }
        // 如果找不到，说明已经失效
        if (StringUtils.isBlank(encryptTokenInRedis))
            throw new AuthenticationException("token已经过期");

        String username = JWTUtil.getUsername(token);

        if (StringUtils.isBlank(username))
            throw new AuthenticationException("token校验不通过");

        // 通过用户名查询用户信息
        User user = userMapper.selectByname(username);

        if (user == null)
            throw new AuthenticationException("用户名或密码错误");
        if (!JWTUtil.verify(token, username, user.getPassword()))
            throw new AuthenticationException("token校验不通过");
        return new SimpleAuthenticationInfo(token, token, "Seckill_realm");
    }
}
