package com.daysources.thirdchallenge.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitSender {

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public RabbitSender(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUserActionMessage(String message) {
        rabbitTemplate.convertAndSend("user.exchange","user.routingkey", message);
    }
}
