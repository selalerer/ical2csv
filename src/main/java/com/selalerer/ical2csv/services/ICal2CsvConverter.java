package com.selalerer.ical2csv.services;

import com.selalerer.ical2csv.model.CalendarEvent;
import com.selalerer.ical2csv.utils.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

        var context = ConversionContext.builder()
                .fromTime(fromTime)
                .toTime(toTime)
                .fromHour(fromHour)
                .toHour(toHour)
                .csvFile(csvFile)
                .build();
        var uidToCalendarEventsMap = readAll(new PathLinesStreamSource(icalFile),
                context);

        convert(context, uidToCalendarEventsMap);
    }

    public void convert(InputStream icalStream, LocalDateTime fromTime, LocalDateTime toTime,
                        int fromHour, int toHour, Path csvFile) {

        var context = ConversionContext.builder()
                .fromTime(fromTime)
                .toTime(toTime)
                .fromHour(fromHour)
                .toHour(toHour)
                .csvFile(csvFile)
                .build();
        var uidToCalendarEventsMap = readAll(new InputStreamLinesStreamSource(icalStream),
                context);

        convert(context, uidToCalendarEventsMap);
    }

    private void convert(ConversionContext context,
                         HashMap<String, List<CalendarEvent>> uidToCalendarEventsMap) {
        var builder = new CalendarTable();

        uidToCalendarEventsMap.values().stream()
                .flatMap(l -> getEventsForGroup(l, context.getToTime()))
                .filter(e -> fitsTimeRange(context, e))
                .forEach(builder::addEventToTable);

        builder.toCsv(context.getCsvFile(), context.getFromHour(), context.getToHour());
    }

    private boolean fitsTimeRange(ConversionContext context, CalendarEvent e) {

        var isWithinLowerBound = context.getFromTimeInZone() == null ||
                !e.getStartTime().isBefore(context.getFromTimeInZone());

        var isWithinUpperBound = context.getToTimeInZone() == null ||
                !e.getEndTime().isAfter(context.getToTimeInZone());

        return isWithinLowerBound && isWithinUpperBound;
    }

    private HashMap<String, List<CalendarEvent>> readAll(LinesStreamSource linesStreamSource,
                                                         ConversionContext context) {

        var uidToCalendarEventsMap = new HashMap<String, List<CalendarEvent>>();

        reader.readAll(linesStreamSource.lines(), e -> uidToCalendarEventsMap
                .computeIfAbsent(e.getUid(), k -> new ArrayList<>())
                .add(e), context::setZoneId);

        return uidToCalendarEventsMap;
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

    @Data
    @Builder
    private static class ConversionContext {
        private ZoneId zoneId;
        private LocalDateTime fromTime;
        private LocalDateTime toTime;
        private int fromHour;
        private int toHour;
        private Path csvFile;

        public LocalDateTime getFromTimeInZone() {
            return DateTimeUtils.migrateTimezone(getFromTime(), ZoneId.systemDefault(), getZoneId());
        }

        public LocalDateTime getToTimeInZone() {
            return DateTimeUtils.migrateTimezone(getToTime(), ZoneId.systemDefault(), getZoneId());
        }
    }
}
