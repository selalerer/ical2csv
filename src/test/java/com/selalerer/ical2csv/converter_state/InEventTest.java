package com.selalerer.ical2csv.converter_state;

import com.selalerer.ical2csv.model.CalendarEvent;
import com.selalerer.ical2csv.model.RepeatRule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InEventTest {

    @Test
    public void parseRepeatedEvent() {

        var input = "BEGIN:VEVENT\n" +
                "DTSTART;TZID=Asia/Jerusalem:20210110T123000\n" +
                "DTEND;TZID=Asia/Jerusalem:20210110T133000\n" +
                "RRULE:FREQ=WEEKLY;WKST=SU;UNTIL=20210306T215959Z;INTERVAL=2\n" +
                "EXDATE;TZID=Asia/Jerusalem:20210221T123000\n" +
                "EXDATE;TZID=Asia/Jerusalem:20210110T123000\n" +
                "EXDATE;TZID=Asia/Jerusalem:20210124T123000\n" +
                "DTSTAMP:20211218T130404Z\n" +
                "UID:7ljcj3nt3nts2lq7glaise95ic@google.com\n" +
                "CREATED:20201213T130909Z\n" +
                "DESCRIPTION:\n" +
                "LAST-MODIFIED:20210220T105024Z\n" +
                "LOCATION:\n" +
                "SEQUENCE:0\n" +
                "STATUS:CONFIRMED\n" +
                "SUMMARY:אור אביגד\n" +
                "TRANSP:OPAQUE\n" +
                "END:VEVENT\n";

        var resultingEvents = new ArrayList<CalendarEvent>();

        var context = ConverterStateContext.of(e -> resultingEvents.add(e),
                LocalDateTime.of(2021, 1, 1, 0, 0),
                LocalDateTime.of(2021, 4, 1, 0, 0),
                ZoneId.of("Asia/Jerusalem"));

        var testSubject = new InEvent(context);

        var lines = input.split("\n");

        for (var line : lines) {
            testSubject.process(line);
        }

        assertEquals(1, resultingEvents.size());

        var resultSourceEvent = resultingEvents.get(0).getSourceEvent();
        assertEquals(LocalDateTime.of(2021, 1, 10, 12, 30),
                resultSourceEvent.getStartTime());
        assertEquals(LocalDateTime.of(2021, 1, 10, 13, 30),
                resultSourceEvent.getEndTime());

        var expectedRR = RepeatRule.of("WEEKLY", "SU",
                LocalDateTime.of(2021, 03, 06, 23, 59, 59),
                2);

        assertEquals(expectedRR, resultSourceEvent.getRepeatRule());

        assertEquals(3, resultSourceEvent.getExceptDates().size());

        assertEquals(LocalDateTime.of(2021, 2, 7, 12, 30),
                resultingEvents.get(0).getStartTime());
    }
}