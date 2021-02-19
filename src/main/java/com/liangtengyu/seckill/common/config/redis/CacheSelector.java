package com.liangtengyu.seckill.common.config.redis;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
