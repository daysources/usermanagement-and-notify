package com.daysources.notify.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitActionListener {

    @RabbitListener(queues = "notify")
    public void handleUserAction(String m){
        System.out.println(m);
    }
}
