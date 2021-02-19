package com.liangtengyu.seckill.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liangtengyu.seckill.common.exception.SeckillException;
import com.liangtengyu.seckill.entity.GoodsAll;
import com.liangtengyu.seckill.entity.GoodsSeckill;
import com.liangtengyu.seckill.entity.OrderInfoSeckill;
import com.liangtengyu.seckill.entity.OrderSeckill;
import com.liangtengyu.seckill.mapper.GoodsSeckillMapper;
import com.liangtengyu.seckill.service.IGoodsAllService;
import com.liangtengyu.seckill.service.IGoodsSeckillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liangtengyu.seckill.service.IOrderInfoSeckillService;
import com.liangtengyu.seckill.service.IOrderSeckillService;
import com.liangtengyu.seckill.service.RedisService;
import com.liangtengyu.seckill.utils.Result;
import com.liangtengyu.seckill.utils.ResultUtil;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lty
 * @since 2021-02-07
 */
@Slf4j
@Service
public class GoodsSeckillServiceImpl extends ServiceImpl<GoodsSeckillMapper, GoodsSeckill> implements IGoodsSeckillService {
    @Autowired
    IGoodsSeckillService goodsSeckillService;
    @Autowired
    IOrderInfoSeckillService orderInfoSeckillService;
    @Autowired
    IOrderSeckillService orderSeckillService;
    @Autowired
    IGoodsAllService goodsAllService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsSeckillMapper goodsSeckillMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    @Synchronized
    public Result generatorOrder(Long goodsId, String username) {
            GoodsSeckill goods = goodsSeckillService.getById(goodsId);
            GoodsAll goodsDesc = goodsAllService.getById(goods.getGoodsId());

            Integer stockCount = goods.getStockCount();
            if (stockCount > 0) {

                String usnm = generatorRandomUserName();

                //插入订单表
                OrderInfoSeckill orderInfoSeckill = new OrderInfoSeckill();
                orderInfoSeckill.setUsername(usnm);
//                orderInfoSeckill.setUsername(username);测试时因为表内不能有重复的用户,随机生成用户id来测试
                orderInfoSeckill.setGoodsId(goodsId);
                orderInfoSeckill.setGoodsName(goodsDesc.getGoodsName());
                orderInfoSeckill.setGoodsCount(1);
                orderInfoSeckill.setGoodsPrice(goodsDesc.getGoodsPrice());
                orderInfoSeckill.setOrderChannel(1);
                orderInfoSeckill.setStatus(0);
                orderInfoSeckill.setCreateDate(LocalDateTime.now());
                orderInfoSeckillService.save(orderInfoSeckill);

                //插入秒杀订单表

                OrderSeckill orderSeckill = new OrderSeckill();
                orderSeckill.setUsername(usnm);
//                orderSeckill.setUsername(username);测试时因为表内不能有重复的用户,随机生成用户id来测试
                orderSeckill.setOrderId(UUID.randomUUID().toString());
                orderSeckill.setGoodsId(goodsId);
                orderSeckill.setOrderInfoId(orderInfoSeckill.getId());
                try {
                    orderSeckillService.save(orderSeckill);
                } catch (Exception e) {
                    if (e instanceof DuplicateKeyException) {
                        throw new SeckillException("您已经抢购下单成功,请不要重复下单!");
                    }
                }
                //此时redis中的库存已经扣掉N个 (有几个请求就扣除几个) ,现在开始扣除数据库中的1个库存
                log.info("更新数据库中的库存数量..");
                int i = goodsSeckillMapper.decrForDatabaseStock(goodsId);
                if (i <= 0) {
                    throw new SeckillException("商品抢购出现异常!");
                }


                return ResultUtil.requestSuccess(orderSeckill.getOrderId());
            }
            throw new SeckillException("商品已被抢购完!");
        }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result generatorOrder_V3(Long goodsId, String username) {
        //此时redis中的库存已经扣掉N个 (有几个请求就扣除几个) ,现在开始扣除数据库中的1个库存
        //使用版本号方式, 保证不会超量
        log.info("更新数据库中的库存数量..");
        int numberOfAttempts = 0;
        int updateResultValue = -1;
        do {
            numberOfAttempts++;
            int goodsVersion = goodsSeckillMapper.getGoodsVersion(goodsId);
            updateResultValue= goodsSeckillMapper.decrForDatabaseStock_V3(goodsId,goodsVersion);
            if (updateResultValue >= 1) {
                break;
            }
        }while (numberOfAttempts <3);



        if (updateResultValue <= 0) {
            return ResultUtil.requestFaild("生成订单重试次数用尽.抢购失败!");
        }


        GoodsSeckill goods = goodsSeckillService.getById(goodsId);
            GoodsAll goodsDesc = goodsAllService.getById(goods.getGoodsId());

            Integer stockCount = goods.getStockCount();
            if (stockCount >= 0) {

                String usnm = generatorRandomUserName();

                //插入订单表
                OrderInfoSeckill orderInfoSeckill = new OrderInfoSeckill();
                orderInfoSeckill.setUsername(usnm);
//                orderInfoSeckill.setUsername(username);测试时因为表内不能有重复的用户,随机生成用户id来测试
                orderInfoSeckill.setGoodsId(goodsId);
                orderInfoSeckill.setGoodsName(goodsDesc.getGoodsName());
                orderInfoSeckill.setGoodsCount(1);
                orderInfoSeckill.setGoodsPrice(goodsDesc.getGoodsPrice());
                orderInfoSeckill.setOrderChannel(1);
                orderInfoSeckill.setStatus(0);
                orderInfoSeckill.setCreateDate(LocalDateTime.now());
                orderInfoSeckillService.save(orderInfoSeckill);

                //插入秒杀订单表

                OrderSeckill orderSeckill = new OrderSeckill();
                orderSeckill.setUsername(usnm);
//                orderSeckill.setUsername(username);测试时因为表内不能有重复的用户,随机生成用户id来测试
                orderSeckill.setOrderId(UUID.randomUUID().toString());
                orderSeckill.setGoodsId(goodsId);
                orderSeckill.setOrderInfoId(orderInfoSeckill.getId());
                try {
                    orderSeckillService.save(orderSeckill);
                } catch (Exception e) {
                    if (e instanceof DuplicateKeyException) {
                        throw new SeckillException("您已经抢购下单成功,请不要重复下单!");
                    }
                }




                return ResultUtil.requestSuccess(orderSeckill.getOrderId());
            }
        return ResultUtil.requestFaild("商品已被抢购完!");
        }

    private String generatorRandomUserName() {
        String s = UUID.randomUUID().toString().split("-")[0];
        return s;
    }


}
