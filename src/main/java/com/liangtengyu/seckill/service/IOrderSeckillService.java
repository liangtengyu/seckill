package com.liangtengyu.seckill.service;

import com.liangtengyu.seckill.entity.OrderSeckill;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lty
 * @since 2021-02-07
 */
public interface IOrderSeckillService extends IService<OrderSeckill> {

    void checkRepateOrder(String username, Long goodsId);
}
