package com.selalerer.ical2csv.utils;

import java.time.ZoneId;

public class ICalUtils {

    public static String getValue(String line) {
        return line.split(":", -1)[1];
    }

    public static ZoneId getTimeZone(String line, ZoneId defaultTz) {
        var field = line.split(":", -1)[0];
        var fieldAndTz = field.split(";", -1);
        if (fieldAndTz.length <= 1) {
            return defaultTz;
        }

        var tzField = fieldAndTz[1];
        if (!tzField.startsWith("TZID=")) {
            return defaultTz;
        }

        return ZoneId.of(tzField.substring("TZID=".length()));
    }
}
