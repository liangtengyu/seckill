package com.liangtengyu.seckill.service;



import com.liangtengyu.seckill.common.exception.SeckillException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {


    /**
     * 获取 redis key 数量
     *
     * @return Map
     */
    Map<String, Object> getKeysSize() throws SeckillException;

    /**
     * 获取 redis 内存信息
     *
     * @return Map
     */
    Map<String, Object> getMemoryInfo() throws SeckillException;

    /**
     * 获取 key
     *
     * @param pattern 正则
     * @return Set
     */
    Set<String> getKeys(String pattern) throws SeckillException;

    /**
     * get命令
     *
     * @param key key
     * @return String
     */
    String get(String key) throws SeckillException;

    /**
     * set命令
     *
     * @param key   key
     * @param value value
     * @return String
     */
    String set(String key, String value) throws SeckillException;

    /**
     * set 命令
     *
     * @param key         key
     * @param value       value
     * @param milliscends 毫秒
     * @return String
     */
    String set(String key, String value, Long milliscends) throws SeckillException;

    /**
     * del命令
     *
     * @param key key
     * @return Long
     */
    Long del(String... key) throws SeckillException;

    /**
     * exists命令
     *
     * @param key key
     * @return Boolean
     */
    Boolean exists(String key) throws SeckillException;

    /**
     * pttl命令
     *
     * @param key key
     * @return Long
     */
    Long pttl(String key) throws SeckillException;

    /**
     * pexpire命令
     *
     * @param key         key
     * @param milliscends 毫秒
     * @return Long
     */
    Long pexpire(String key, Long milliscends) throws SeckillException;


    /**
     * zadd 命令
     *
     * @param key    key
     * @param score  score
     * @param member value
     */
    Long zadd(String key, Double score, String member) throws SeckillException;

    /**
     * zrangeByScore 命令
     *
     * @param key key
     * @param min min
     * @param max max
     * @return Set<String>
     */
    Set<String> zrangeByScore(String key, String min, String max) throws SeckillException;

    /**
     * zremrangeByScore 命令
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return Long
     */
    Long zremrangeByScore(String key, String start, String end) throws SeckillException;

    /**
     * zrem 命令
     *
     * @param key     key
     * @param members members
     * @return Long
     */
    Long zrem(String key, String... members) throws SeckillException;
    /**
     * 自增1 命令
     *
     * @param key     key
     * @return Long
     */
    Long increment(String key) throws SeckillException;

    /**
     * 递减1 命令
     *
     * @param key     key
     * @return Long
     */
    Long decrement(String key) throws SeckillException;
}
