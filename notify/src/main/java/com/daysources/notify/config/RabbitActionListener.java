package com.daysources.notify.config;

import com.daysources.notify.config.log.LogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component @RequiredArgsConstructor
public class RabbitActionListener {

    private final MongoTemplate mongoTemplate;

    @RabbitListener(queues = "notify")
    public void handleUserAction(String m){
        System.out.println(m);
        LogEntity log = new LogEntity();
        log.setMessage(m);
        log.setTimestamp(new Date());
        mongoTemplate.save(log);
    }
}
