package com.liangtengyu.seckill.common.config.redis;

import com.liangtengyu.seckill.common.exception.SeckillException;

@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws SeckillException;
}
