package com.liangtengyu.seckill.service.impl;

import com.liangtengyu.seckill.common.exception.SeckillException;
import com.liangtengyu.seckill.entity.OrderSeckill;
import com.liangtengyu.seckill.mapper.OrderSeckillMapper;
import com.liangtengyu.seckill.service.IOrderSeckillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lty
 * @since 2021-02-07
 */
@Service
public class OrderSeckillServiceImpl extends ServiceImpl<OrderSeckillMapper, OrderSeckill> implements IOrderSeckillService {

    @Autowired
    OrderSeckillMapper orderSeckillMapper;
    @Override
    public void checkRepateOrder(String username, Long goodsId) {
        int count= orderSeckillMapper.checkRepateOrder(username, goodsId);
        if (count > 1) {
            throw new SeckillException("已经下单过了 请勿重复下单哦!");
        }
    }
}
