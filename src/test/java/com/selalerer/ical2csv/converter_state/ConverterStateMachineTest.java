package com.selalerer.ical2csv.converter_state;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ConverterStateMachineTest {

    @Test
    public void exceptDates() {
        var eventStr = """
                BEGIN:VEVENT
                DTSTART;TZID=Asia/Jerusalem:20221124T083000
                DTEND;TZID=Asia/Jerusalem:20221124T093000
                RRULE:FREQ=WEEKLY;UNTIL=20230614T205959Z;BYDAY=TH
                EXDATE;TZID=Asia/Jerusalem:20221124T083000
                EXDATE;TZID=Asia/Jerusalem:20221201T083000
                EXDATE;TZID=Asia/Jerusalem:20221208T083000
                EXDATE;TZID=Asia/Jerusalem:20221215T083000
                EXDATE;TZID=Asia/Jerusalem:20221222T083000
                EXDATE;TZID=Asia/Jerusalem:20221229T083000
                EXDATE;TZID=Asia/Jerusalem:20230119T083000
                EXDATE;TZID=Asia/Jerusalem:20230316T083000
                EXDATE;TZID=Asia/Jerusalem:20230406T083000
                EXDATE;TZID=Asia/Jerusalem:20230525T083000
                DTSTAMP:20230909T051628Z
                UID:4aidcbsrsck4md1ttbsmkev9ia_R20221124T063000@google.com
                CREATED:20221109T070119Z
                LAST-MODIFIED:20230523T090026Z
                SEQUENCE:2
                STATUS:CONFIRMED
                SUMMARY:טל עופרי-
                TRANSP:OPAQUE
                END:VEVENT
                """;

        AtomicInteger eventCount = new AtomicInteger(0);

        var testSubject = new ConverterStateMachine(e -> {
            eventCount.incrementAndGet();
        }, null);

        for (var line : eventStr.split("\n")) {
            testSubject.process(line);
        }

        assertEquals(1, eventCount.get());
    }

}