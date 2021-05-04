package com.selalerer.ical2csv.converter_state;

import com.opencsv.CSVWriter;

import java.time.LocalDateTime;

public class ConverterStateMachine {

    private ConverterState state;

    public ConverterStateMachine(CSVWriter writer, LocalDateTime fromTime, LocalDateTime toTime) {
        state = new NotInEvent(writer, fromTime, toTime);
    }

    public boolean process(String line) {
        state = state.process(line);
        return state != null;
    }
}
