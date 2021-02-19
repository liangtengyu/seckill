package com.liangtengyu.seckill.common.config.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置bean
 */
@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";
    public static final String TEST_QUEUE = "test_queue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }
    @Bean
    public Queue testQueue() {
        return new Queue(TEST_QUEUE, true);
    }

}
