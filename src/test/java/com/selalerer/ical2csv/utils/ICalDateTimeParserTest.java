package com.selalerer.ical2csv.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ICalDateTimeParserTest {

    @Test
    public void test1() {
        var testSubject = new ICalDateTimeParser();

        var result = testSubject.parse("20110406T143000Z");
        var expected = LocalDateTime.of(2011, 04, 06, 14, 30, 00);

        assertEquals(expected, result);
    }
}