package com.selalerer.ical2csv.utils;

public class ICalUtils {

    public static String getValue(String line) {
        return line.split(":", -1)[1];
    }
}
