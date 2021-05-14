package com.selalerer.ical2csv.converter_state;

import com.opencsv.CSVWriter;
import com.selalerer.ical2csv.model.CalendarEvent;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public class NotInEvent implements ConverterState {

    private final ConverterStateContext context;

    public NotInEvent(ConverterStateContext context) {
        this.context = context;
    }

    @Override
    public ConverterState process(String line) {
        if ("BEGIN:VEVENT".equals(line)) {
            return new InEvent(context);
        }

        if ("BEGIN:VTIMEZONE".equals(line)) {
            return new InTimezone(context);
        }

        return this;
    }
}
