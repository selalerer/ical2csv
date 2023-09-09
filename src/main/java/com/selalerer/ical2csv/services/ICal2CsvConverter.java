package com.selalerer.ical2csv.services;

import com.selalerer.ical2csv.model.CalendarEvent;
import com.selalerer.ical2csv.utils.ICalUtils;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ICal2CsvConverter {

    private final ICalReader reader;

    public ICal2CsvConverter(ICalReader reader) {
        this.reader = reader;
    }

    public void convert(Path icalFile, LocalDateTime fromTime, LocalDateTime toTime,
                        int fromHour, int toHour, Path csvFile) {

        var uidToCalendarEventsMap = new HashMap<String, List<CalendarEvent>>();

        reader.readAll(icalFile, fromTime, toTime, e -> uidToCalendarEventsMap
                .computeIfAbsent(e.getUid(), k -> new ArrayList<>())
                .add(e));

        var builder = new CalendarTable();

        uidToCalendarEventsMap.values().stream()
                .flatMap(l -> getEventsForGroup(l, toTime))
                .forEach(builder::addEventToTable);

        builder.toCsv(csvFile, fromHour, toHour);
    }

    private Stream<CalendarEvent> getEventsForGroup(List<CalendarEvent> calendarEvents,
                                                    LocalDateTime toTime) {
        var rruleCalendarEvents = calendarEvents.stream()
                .filter(e -> e.getRepeatRule() != null)
                .toList();
        if (rruleCalendarEvents.isEmpty()) {
            return calendarEvents.stream();
        }
        var rruleEvent = unifyRruleEvents(rruleCalendarEvents);

        calendarEvents.stream()
                .map(CalendarEvent::getRecurrenceId)
                .filter(Objects::nonNull)
                .forEach(rruleEvent.getExceptDates()::add);

        return Stream.concat(
            calendarEvents.stream().filter(e -> e.getRepeatRule() == null),
            ICalUtils.splitToRepeatedEventsIfNecessary(rruleEvent, toTime).stream()
        );
    }

    private CalendarEvent unifyRruleEvents(List<CalendarEvent> rruleCalendarEvents) {
        var rruleEvent = rruleCalendarEvents.get(0);
        if (rruleEvent.getExceptDates() == null) {
            rruleEvent.setExceptDates(new ArrayList<>());
        }
        for (var i = 1; i < rruleCalendarEvents.size(); ++i) {
            var e = rruleCalendarEvents.get(i);
            if (e.getExceptDates() != null) {
                rruleEvent.getExceptDates().addAll(e.getExceptDates());
            }
        }
        return rruleEvent;
    }
}
