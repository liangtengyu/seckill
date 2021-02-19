package com.liangtengyu.seckill.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import com.liangtengyu.seckill.common.authentication.JWTFilter;
import com.liangtengyu.seckill.common.authentication.JWTUtil;
import com.liangtengyu.seckill.common.config.mq.MQSender;
import com.liangtengyu.seckill.common.exception.SeckillException;
import com.liangtengyu.seckill.entity.GoodsAll;
import com.liangtengyu.seckill.entity.GoodsSeckill;
import com.liangtengyu.seckill.entity.OrderInfoSeckill;
import com.liangtengyu.seckill.entity.OrderSeckill;
import com.liangtengyu.seckill.service.IGoodsAllService;
import com.liangtengyu.seckill.service.IGoodsSeckillService;
import com.liangtengyu.seckill.service.IOrderInfoSeckillService;
import com.liangtengyu.seckill.service.IOrderSeckillService;
import com.liangtengyu.seckill.service.RedisService;
import com.liangtengyu.seckill.utils.CommonUtil;
import com.liangtengyu.seckill.utils.Result;
import com.liangtengyu.seckill.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author lty
 * @since 2021-02-07
 */

@Slf4j
@RestController
@RequestMapping("/goods-seckill")
public class GoodsSeckillController  implements InitializingBean {

    private static final Double DEFAULT_PermitsPerSecond = 20d;
    //基于令牌桶算法的限流实现类
    RateLimiter rateLimiter = RateLimiter.create(DEFAULT_PermitsPerSecond);
    //处理标记,为true表示被处理过的商品,false表示可以处理
    private final HashMap<Long, Boolean> localOverMap = new HashMap<>();


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
    MQSender mqSender;

    @RequestMapping("/list")
    public Result list(){
        List<GoodsSeckill> list = goodsSeckillService.list();
        return ResultUtil.requestSuccess(list);
    }

    //region 第0个版本的秒杀
    //第0个版本,基础的下单 总商品9998件 使用1000线程循环10次
    // QPS:1,682.369    RT-AVERAGE:245ms   Samples:10000
    @RequestMapping("/seckill_v0")
    public Result seckill0(Long goodsId, HttpServletRequest request) {
        String username = getUsername(request);
        return goodsSeckillService.generatorOrder(goodsId, username);
    }//endregion


    //region 第一个版本的秒杀
    //第一个版本,基础的下单 使用1000线程循环10次
    // QPS:1,024.8    RT-AVERAGE:17ms   Samples:10000
    @RequestMapping("/seckill_v1")
    public Result seckill1(Long goodsId, HttpServletRequest request) {
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            return ResultUtil.requestFaild("活动太火爆啦!再试试吧!");
        }
        String username = getUsername(request);
        return goodsSeckillService.generatorOrder(goodsId, username);
    }//endregion



    //region 第2个版本的秒杀
    //第2个版本 总商品9998件 使用1000线程循环10次 剩余9797件
    // QPS:1109.75    RT-AVERAGE:16ms   Samples:10000
    //默认的每秒许可证


    @RequestMapping("/seckill_v2")
    public Result seckillv2(Long goodsId, HttpServletRequest request) {
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            return ResultUtil.requestFaild("活动太火爆啦!再试试吧!");
        }
        //内存标记此商品ID是否被抢空，减少redis访问
        boolean end = localOverMap.get(goodsId);

        if (end) {
            return ResultUtil.requestFaild("此商品已被抢购一空,请下次活动再试试吧!");
        }

        String username = getUsername(request);

        //1.先对此商品的数量-1 看看情况 如果 <0 说明库存被清空或者该KEY在redis中不存在 此时去加载数据库中的库存数据
        Long reserve = redisService.decrement(goodsId + "");
        log.info("在redis中现在goodid:{} 的商品还剩下{}件",goodsId,reserve);
        if (reserve < 0d) {
            //第一次< 0 我们可以认为没有这个Key 再加载一次库存数,进行初始化
            loadDatabaseGoodsIdCount();
            //加载完数据后再次-1
            Long reserve2 = redisService.decrement(goodsId + "");
            if (reserve2 < 0) {
                //第2次是从数据库中加载的总数 此时如果 再为空 说明真没库存了 将内存标记改为商品被抢完
                localOverMap.put(goodsId, true);
                log.info("商品ID:{} 的秒杀结束!内存标记为true",goodsId);
                return ResultUtil.requestFaild("此商品已被抢购一空,请下次活动再试试吧!");
            }
        }
        //去下单
        return goodsSeckillService.generatorOrder(goodsId, username);
    }//endregion

    //region 第3个版本的秒杀

    //第3个版本 总商品9998件 使用1000线程循环10次 剩余9797件
     //QPS 2805.83  RT-AVERAGE:92ms   Samples:10000


    @RequestMapping("/seckill_v3")
    public Result seckillv3(Long goodsId, HttpServletRequest request) {
        if (!rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            return ResultUtil.requestFaild("活动太火爆啦!再试试吧!");
        }
        //内存标记此商品ID是否被抢空，减少redis访问
        boolean end = localOverMap.get(goodsId);

        if (end) {
            return ResultUtil.requestFaild("此商品已被抢购一空,请下次活动再试试吧!");
        }

        String username = getUsername(request);

        //1.预减库存 先对此商品的数量-1 看看情况 如果 <0 说明库存被清空或者该KEY在redis中不存在 此时去加载数据库中的库存数据
        Long reserve = redisService.decrement(goodsId + "");
        log.info("在redis中现在goodid:{} 的商品还剩下{}件",goodsId,reserve);
        if (reserve < 0d) {
            //第一次< 0 我们可以认为没有这个Key 再加载一次库存数,进行初始化
            loadDatabaseGoodsIdCount();
            //加载完数据后再次-1
            Long reserve2 = redisService.decrement(goodsId + "");
            if (reserve2 < 0) {
                //第2次是从数据库中加载的总数 此时如果 再为空 说明真没库存了 将内存标记改为商品被抢完
                localOverMap.put(goodsId, true);
                log.info("商品ID:{} 的秒杀结束!内存标记为true",goodsId);
                return ResultUtil.requestFaild("此商品已被抢购一空,请下次活动再试试吧!");
            }
        }
        //2. 前置判断重复的下单,  V1 和 V2版本中是在下单时判断的
        orderSeckillService.checkRepateOrder(username,goodsId);
        //3.构建一个JSON
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("goodsId", goodsId.toString());
        mqSender.sendSeckillMessage(json.toJSONString());
        //进入队列下单,将秒杀业务解耦
        return ResultUtil.requestSuccess(null);
    }


    /**
     * 从token中获取用户名
     * @param request
     * @return
     */
    private String getUsername(HttpServletRequest request) {
        return JWTUtil.getUsername(CommonUtil.decryptToken(request.getHeader(JWTFilter.TOKEN)));
    }


    //bean初始化时 加载
    @Override
    public void afterPropertiesSet() throws Exception {
        loadDatabaseGoodsIdCount();
    }

    /**
     * 系统初始化,将商品信息加载到redis和本地内存
     */
    private void loadDatabaseGoodsIdCount() {
        //加载数据
        List<GoodsSeckill> list = goodsSeckillService.list();

        if (list == null) {
            log.info("库存信息加载获取为Null");
            return;
        }
        for (GoodsSeckill goodsSeckill : list) {
            Long goodsId = goodsSeckill.getGoodsId();
            redisService.set("" + goodsId, "" +goodsSeckill.getStockCount());//此处可以控制每批次只出多少库存,按比例给redis分配,目前是直接全部库存加载进redis
            localOverMap.put(goodsId, false);
        }
        log.info("商品信息加载到redis和本地内存完毕!");

    }
    //endregion
}
