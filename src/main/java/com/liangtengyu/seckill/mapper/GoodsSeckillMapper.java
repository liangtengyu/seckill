package com.liangtengyu.seckill.mapper;

import com.liangtengyu.seckill.entity.GoodsSeckill;
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
public interface GoodsSeckillMapper extends BaseMapper<GoodsSeckill> {

    int decrForDatabaseStock(long goodsId);
    int decrForDatabaseStock_V3(long goodsId,Integer version);
    int getGoodsVersion(long goodsId);

    int getRealInventory(long goodsId);
}
