package com.liangtengyu.seckill.common.config.mq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;


    /**
     * 测试队列
     * @param msg
     */
	public void sendSeckillMessageToTestQueue(String msg){
        log.info("MQ测试队列  send message:{}",msg);
        amqpTemplate.convertAndSend(MQConfig.TEST_QUEUE, msg);

    }



    /**
     * 正式的队列
     * @param msg
     */
	public void sendSeckillMessage(String msg){
        log.info("MQ send message:{}",msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);

    }
}
