package com.selalerer.ical2csv.utils;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

public class ICalUtilsTest {

    @Test
    public void getTimeZone_positive() {
        var result = ICalUtils.getTimeZone("DTSTART;TZID=Asia/Jerusalem:20210107T140000", ZoneId.of("UTC"));

        assertEquals("Asia/Jerusalem", result.toString());
    }

    @Test
    public void getTimeZone_negative() {
        var result = ICalUtils.getTimeZone("DTSTART:20210107T140000", ZoneId.of("UTC"));

        assertEquals("UTC", result.toString());
    }

    @Test
    public void getValue_positive() {
        var result = ICalUtils.getValue("DTSTART:20210107T140000");

        assertEquals("20210107T140000", result);
    }

    @Test
    public void getValue_positive_withTimezone() {
        var result = ICalUtils.getValue("DTSTART;TZID=Asia/Jerusalem:20210107T140000");

        assertEquals("20210107T140000", result);
    }

}