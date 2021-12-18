package com.selalerer.ical2csv.utils;

import com.selalerer.ical2csv.model.CalendarEvent;
import com.selalerer.ical2csv.model.RepeatRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

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

    @Test
    public void parseRepeatRule() {

        var repeatRule = "FREQ=WEEKLY;WKST=SU;UNTIL=20210306T215959Z;INTERVAL=2";

        var result = ICalUtils.parseRepeatRule(repeatRule, ZoneId.of("UTC"), ZoneId.of("UTC"));

        var expected = RepeatRule.of("WEEKLY", "SU",
                LocalDateTime.of(2021, 03, 06, 21, 59, 59),
                2);

        assertEquals(expected, result);
    }

    @Test
    public void splitToRepeatedEventsIfNecessary() {

        var sourceEvent = new CalendarEvent();
        sourceEvent.setStartTime(LocalDateTime.of(2021, 1, 10, 12, 30));
        sourceEvent.setEndTime(LocalDateTime.of(2021, 1, 10, 13, 30));
        sourceEvent.setSummary("hello");
        sourceEvent.setDescription("wow!");
        sourceEvent.setLocation("Over there!");
        sourceEvent.setStatus("CONFIRMED");
        sourceEvent.setExceptDates(List.of(
                LocalDateTime.of(2021, 2, 21, 12, 30),
                LocalDateTime.of(2021, 1, 10, 12, 30),
                LocalDateTime.of(2021, 1, 24, 12, 30)
        ));
        sourceEvent.setRepeatRule(RepeatRule.of("WEEKLY", "SU",
                LocalDateTime.of(2021, 03, 06, 21, 59, 59),
                2));

        var results = ICalUtils.splitToRepeatedEventsIfNecessary(sourceEvent,
                LocalDateTime.of(2021, 4, 1, 0, 0));

        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(e -> e.getSummary().equals("hello")));
        assertTrue(results.stream().anyMatch(e -> e.getStartTime().equals(
                LocalDateTime.of(2021, 2, 7, 12, 30)
        )));
    }

}