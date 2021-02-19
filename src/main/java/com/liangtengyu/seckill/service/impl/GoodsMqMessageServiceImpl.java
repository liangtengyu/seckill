package com.liangtengyu.seckill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liangtengyu.seckill.mapper.GoodsSeckillMapper;
import com.liangtengyu.seckill.service.GoodsMqMessageService;
import com.liangtengyu.seckill.service.IGoodsSeckillService;
import com.liangtengyu.seckill.service.IOrderSeckillService;
import com.liangtengyu.seckill.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * @Author: lty
 * @Date: 2021/2/18 09:54
 */
@Service
@Slf4j
public class GoodsMqMessageServiceImpl implements GoodsMqMessageService {

    @Autowired
    IOrderSeckillService orderSeckillService;

    @Autowired
    GoodsSeckillMapper goodsSeckillMapper;

    @Autowired
    IGoodsSeckillService goodsSeckillService;

    @Override
    public JSONObject covertToJsonObj(String msg) {
        return JSONObject.parseObject(msg);
    }


    /**
     * 处理秒杀
     * @param json
     * @return
     */
    @Override
    public String HandlerSeckill(JSONObject json) {
        log.info("MQ接收到秒杀信息{} ,现在开始处理秒杀处理库存逻辑...",json);
        String username = json.getString("username");
        Long goodsId = json.getLong("goodsId");
        //Verify inventory quantity
        int realInventory = goodsSeckillMapper.getRealInventory(goodsId);
        if (realInventory < 1) {
            return ("此商品已被抢购一空,请下次活动再试试吧!");
        }
        //VerifyRepeatedOrders
        orderSeckillService.checkRepateOrder(username, goodsId);
        //Place an order     Reduce inventory
        Result result = null;
        try {
             result = goodsSeckillService.generatorOrder_V3(goodsId, username);
        } catch (Exception e) {
            log.info(e.getMessage());
            return e.getMessage();
        }
        return result.getMsg();
    }
}
