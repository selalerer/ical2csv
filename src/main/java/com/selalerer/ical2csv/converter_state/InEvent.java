package com.selalerer.ical2csv.converter_state;

import com.opencsv.CSVWriter;
import com.selalerer.ical2csv.model.CalendarEvent;
import com.selalerer.ical2csv.utils.ICalDateTimeParser;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Slf4j
public class InEvent implements ConverterState {

    private final Consumer<CalendarEvent> consumer;
    private final LocalDateTime fromTime;
    private final LocalDateTime toTime;
    private final CalendarEvent event = new CalendarEvent();
    private final ICalDateTimeParser dateTimeParser = new ICalDateTimeParser();

    public InEvent(Consumer<CalendarEvent> consumer, LocalDateTime fromTime, LocalDateTime toTime) {
        this.consumer = consumer;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    @Override
    public ConverterState process(String line) {
        log.debug("line={}", line);

        if ("END:VEVENT".equals(line)) {
            if (event.getStartTime() != null) {
                if (!event.getStartTime().isBefore(fromTime)) {
                    consumer.accept(event);
                }

                if (event.getStartTime().isAfter(toTime)) {
                    // No need to continue reading events
                    // (assuming events are sorted by start time)
                    return null;
                }
            }
            return new NotInEvent(consumer, fromTime, toTime);
        }

        if (line.startsWith("DTSTART:")) {
            event.setStartTime(dateTimeParser.parse(getValue(line)));
        } else if (line.startsWith("DTEND:")) {
            event.setEndTime(dateTimeParser.parse(getValue(line)));
        } else if (line.startsWith("DESCRIPTION:")) {
            event.setDescription(getValue(line));
        } else if (line.startsWith("LOCATION:")) {
            event.setLocation(getValue(line));
        } else if (line.startsWith("STATUS:")) {
            event.setStatus(getValue(line));
        } else if (line.startsWith("SUMMARY:")) {
            event.setSummary(getValue(line));
        }

        return this;
    }

    private static String getValue(String line) {
        return line.split(":", -1)[1];
    }
}
