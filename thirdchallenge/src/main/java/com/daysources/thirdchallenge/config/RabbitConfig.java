package com.daysources.thirdchallenge.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

        @Bean
        public TopicExchange exchange() {
            return new TopicExchange("user.exchange");
        }

        @Bean
        public Queue queue() {
            return new Queue("notify");
        }

        @Bean
        public Binding binding(Queue queue, TopicExchange exchange) {
            return BindingBuilder.bind(queue).to(exchange).with("user.routingkey");
        }
}
