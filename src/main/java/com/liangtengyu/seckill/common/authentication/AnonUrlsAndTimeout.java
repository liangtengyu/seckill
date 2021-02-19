package com.liangtengyu.seckill.common.authentication;

import lombok.Data;

/**
 * @Author: lty
 * @Date: 2021/2/7 10:20
 */
@Data
public class AnonUrlsAndTimeout {
    public static final int DEFAULT_TIMEOUT = 86400;
    public static final String URLS = "/user/**,/logout/**,/doc.html,/getCaptcha,/test";


    //启动时加载,用来过滤 不需要拦截 的路径
    public static final String urls = URLS;
    //token 过期时间
    public static final long timeout = DEFAULT_TIMEOUT;
}
