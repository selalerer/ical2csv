package com.selalerer.ical2csv.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

public class ICalDateTimeParserTest {

    @Test
    public void parse_noTimezone() {
        var testSubject = new ICalDateTimeParser();

        var result = testSubject.parse("20110406T143000Z", null);
        var expected = LocalDateTime.of(2011, 04, 06, 14, 30, 00);

        assertEquals(expected, result);
    }

    @Test
    public void parse_withTimezone() {
        var testSubject = new ICalDateTimeParser();

        var result = testSubject.parse("20110406T143000Z", ZoneId.of("Asia/Jerusalem"));
        var expected = LocalDateTime.of(2011, 04, 06, 17, 30, 00);

        assertEquals(expected, result);
    }

}