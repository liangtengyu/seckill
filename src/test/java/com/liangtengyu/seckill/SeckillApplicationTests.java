package com.liangtengyu.seckill;

import com.liangtengyu.seckill.common.authentication.JWTUtil;
import com.liangtengyu.seckill.common.config.mq.MQSender;
import com.liangtengyu.seckill.utils.CommonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeckillApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void Test(){
        System.out.println(JWTUtil.getUsername(CommonUtil.decryptToken("a130424a475410f7fc7b57a1f0b825f44ec7021e0b479e2f5360b97fddcb8ab9eff3c714fa7fed17f10fc8a21ec297d8aba366f51431f4541da246962834f4752154b70a66dcea89891b2b156226cbbbb3cf1586a3b1db73ae8148ee6c3c7458dbbd191dfa027ae5db71132edd31fafb3656b5c3db47ae6d555ab1c2b6283225a3721b1a3334efb0")));
    }


    //MQ发送订单信息测试
    @Autowired
    MQSender mqSender;
    @Test
    public void TestSendMqMessage(){
        mqSender.sendSeckillMessage("2021-02-08 16:55:47.085 [main] INFO  o.s.a.r.c.CachingConnectionFactory - Created new connection: rabbitConnectionFactory#152c4495:0/SimpleConnection@6253e59a [delegate=amqp://guest@127.0.0.1:5672/, localPort= 57548] \n");
    }


    @Test
    public void TestSendMqMessageTest(){
        mqSender.sendSeckillMessageToTestQueue("2021-02-08 16:55:47.085 [main] INFO  o.s.a.r.c.CachingConnectionFactory - Created new connection: rabbitConnectionFactory#152c4495:0/SimpleConnection@6253e59a [delegate=amqp://guest@127.0.0.1:5672/, localPort= 57548] \n");
    }
}
