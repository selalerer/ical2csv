package com.selalerer.ical2csv.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ICalDateTimeParser {

    public static LocalDateTime parse(String icalDateTime, ZoneId fromTimeZone, ZoneId toTimezone) {
        // YYYYMMDDTHHmmSS
        var year = Integer.parseInt(icalDateTime.substring(0, 4));
        var month = Integer.parseInt(icalDateTime.substring(4,6));
        var day = Integer.parseInt(icalDateTime.substring(6, 8));

        var hour = Integer.parseInt(icalDateTime.substring(9, 11));
        var minute = Integer.parseInt(icalDateTime.substring(11, 13));
        var second = Integer.parseInt(icalDateTime.substring(13, 15));

        var dateTime = LocalDateTime.of(year, month, day, hour, minute, second);

        return DateTimeUtils.migrateTimezone(dateTime, fromTimeZone, toTimezone);
    }
}
