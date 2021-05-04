package com.selalerer.ical2csv.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ICalDateTimeParser {

    public LocalDateTime parse(String icalDateTime) {
        // YYYYMMDDTHHmmSS
        var year = Integer.parseInt(icalDateTime.substring(0, 4));
        var month = Integer.parseInt(icalDateTime.substring(4,6));
        var day = Integer.parseInt(icalDateTime.substring(6, 8));

        var hour = Integer.parseInt(icalDateTime.substring(9, 11));
        var minute = Integer.parseInt(icalDateTime.substring(11, 13));
        var second = Integer.parseInt(icalDateTime.substring(13, 15));

        return LocalDateTime.of(year, month, day, hour, minute, second);
    }
}
