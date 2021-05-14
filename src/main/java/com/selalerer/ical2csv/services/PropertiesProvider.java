package com.selalerer.ical2csv.services;

import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Service
public class PropertiesProvider {

    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private int fromHour;
    private int toHour;

    @PostConstruct
    public void init() {
        var fromTimeStr = System.getProperty("fromTime", LocalDateTime.MIN.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        var toTimeStr = System.getProperty("toTime", LocalDateTime.MAX.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        this.fromTime = LocalDateTime.parse(fromTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.toTime = LocalDateTime.parse(toTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        this.fromHour = Integer.parseInt(System.getProperty("fromHour", "8"));
        this.toHour = Integer.parseInt(System.getProperty("toHour", "21"));
    }
}
