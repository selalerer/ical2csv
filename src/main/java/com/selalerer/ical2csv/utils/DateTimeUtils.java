package com.selalerer.ical2csv.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeUtils {

    public static LocalDateTime toLocalDateTime(Date d) {
        return Instant.ofEpochMilli(d.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
