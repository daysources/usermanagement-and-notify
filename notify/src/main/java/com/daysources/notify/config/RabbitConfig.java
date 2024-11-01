package com.daysources.notify.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration @EnableRabbit
public class RabbitConfig {

    @Bean
    public Queue myQueue () {
        return new Queue("notify", true);
    }
}
