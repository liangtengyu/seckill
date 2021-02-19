package com.liangtengyu.seckill.service;

import com.liangtengyu.seckill.entity.GoodsSeckill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liangtengyu.seckill.utils.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lty
 * @since 2021-02-07
 */
public interface IGoodsSeckillService extends IService<GoodsSeckill> {
    Result generatorOrder(Long goodsId, String username);
    Result generatorOrder_V3(Long goodsId, String username);
}
