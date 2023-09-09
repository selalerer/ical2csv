package com.selalerer.ical2csv.utils;

import com.selalerer.ical2csv.model.CalendarEvent;
import com.selalerer.ical2csv.model.RepeatRule;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public static RepeatRule parseRepeatRule(String repeatRuleString, ZoneId fromTimeZone, ZoneId toTimezone) {

        var params = repeatRuleString.split(";", -1);

        var result = new RepeatRule();

        for (var param : params) {
            var parts = param.split("=", -1);
            if (parts.length < 2) {
                continue;
            }
            var key = parts[0];
            var value = parts[1];

            switch (key) {
                case "FREQ" -> result.setFrequency(value);
                case "WKST" -> result.setWeekStart(value);
                case "UNTIL" -> result.setUntil(ICalDateTimeParser.parse(value, fromTimeZone, toTimezone));
                case "INTERVAL" -> result.setInterval(Integer.parseInt(value));
            }
        }

        return result;
    }

    public static List<CalendarEvent> splitToRepeatedEventsIfNecessary(CalendarEvent sourceEvent, LocalDateTime defaultUntil) {
        if (sourceEvent.getRepeatRule() == null) {
            return List.of(sourceEvent);
        }

        var exceptDate = Optional.ofNullable(sourceEvent.getExceptDates())
                .orElseGet(() -> List.of());
        var result = new ArrayList<CalendarEvent>();

        if (!exceptDate.contains(sourceEvent.getStartTime())) {
            result.add(sourceEvent);
        }

        var eventTime = sourceEvent.getStartTime();
        var eventEndTime = sourceEvent.getEndTime();
        var until = Optional.ofNullable(sourceEvent.getRepeatRule().getUntil())
                .orElse(defaultUntil);
        var frequencyInDays = frequencyToDays(sourceEvent.getRepeatRule().getFrequency());

        if (frequencyInDays == -1) {
            return result;
        }

        while (true) {
            for (var time = 0 ; time < sourceEvent.getRepeatRule().getInterval() ; ++time) {
                eventTime = eventTime.plusDays(frequencyInDays);
                eventEndTime = eventEndTime.plusDays(frequencyInDays);
            }
            if (!eventTime.isBefore(until)) {
                break;
            }

            if (exceptDate.contains(eventTime)) {
                continue;
            }

            var newEvent = sourceEvent.toBuilder()
                    .repeatRule(null)
                    .exceptDates(null)
                    .startTime(eventTime)
                    .endTime(eventEndTime)
                    .sourceEvent(sourceEvent)
                    .build();

            result.add(newEvent);
        }

        return result;
    }

    private static int frequencyToDays(String frequency) {
        if (frequency == null) {
            return -1;
        }
        return switch (frequency) {
            case "WEEKLY" -> 7;
            default -> -1;
        };
    }
}
