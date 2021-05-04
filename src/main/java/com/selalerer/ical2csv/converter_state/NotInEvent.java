package com.selalerer.ical2csv.converter_state;

import com.opencsv.CSVWriter;

import java.time.LocalDateTime;

public class NotInEvent implements ConverterState {

    private final CSVWriter writer;
    private final LocalDateTime fromTime;
    private final LocalDateTime toTime;

    public NotInEvent(CSVWriter writer, LocalDateTime fromTime, LocalDateTime toTime) {
        this.writer = writer;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    @Override
    public ConverterState process(String line) {
        return "BEGIN:VEVENT".equals(line) ?
                new InEvent(writer, fromTime, toTime) :
                this;
    }
}
