package com.daysources.notify.config.log;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("datalogs") @Data
public class LogEntity {

    @Id
    private String id;
    private String message;
    private Date timestamp;
}
