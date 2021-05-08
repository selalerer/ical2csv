package com.selalerer.ical2csv.converter_state;

import com.opencsv.CSVWriter;
import com.selalerer.ical2csv.model.CalendarEvent;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public class NotInEvent implements ConverterState {

    private final Consumer<CalendarEvent> consumer;
    private final LocalDateTime fromTime;
    private final LocalDateTime toTime;

    public NotInEvent(Consumer<CalendarEvent> consumer, LocalDateTime fromTime, LocalDateTime toTime) {
        this.consumer = consumer;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    @Override
    public ConverterState process(String line) {
        return "BEGIN:VEVENT".equals(line) ?
                new InEvent(consumer, fromTime, toTime) :
                this;
    }
}
