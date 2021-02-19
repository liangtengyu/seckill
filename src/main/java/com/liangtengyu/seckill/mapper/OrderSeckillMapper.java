package com.liangtengyu.seckill.mapper;

import com.liangtengyu.seckill.entity.OrderSeckill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lty
 * @since 2021-02-07
 */
@Repository
public interface OrderSeckillMapper extends BaseMapper<OrderSeckill> {

    int checkRepateOrder(String username, Long goodsId);
}
