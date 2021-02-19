package com.liangtengyu.seckill.common.config.mq;


import com.alibaba.fastjson.JSONObject;
import com.liangtengyu.seckill.service.GoodsMqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MQReceiver {
    @Autowired
    GoodsMqMessageService goodsMqMessageService;

    @RabbitListener(queues=MQConfig.TEST_QUEUE)
    public void receiveTest(String message){
        log.info("MQ测试队列 receive message:{}",message);
    }


    /**
     * 接收MQ队列消息
     * @param message
     */
    @RabbitListener(queues=MQConfig.QUEUE)
    public void receive(String message){
        JSONObject json = goodsMqMessageService.covertToJsonObj(message);
        //处理订单
        String res = goodsMqMessageService.HandlerSeckill(json);
        log.info(res);
    }

}
