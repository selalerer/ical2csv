package com.selalerer.ical2csv.converter_state;

import com.opencsv.CSVWriter;
import com.selalerer.ical2csv.model.CalendarEvent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Consumer;

public class ConverterStateMachine {

    private ConverterState state;

    public ConverterStateMachine(Consumer<CalendarEvent> consumer,
                                 Consumer<ZoneId> zoneIdConsumer) {
        state = new NotInEvent(ConverterStateContext.of(consumer, zoneIdConsumer, null));
    }

    public boolean process(String line) {
        state = state.process(line);
        return state != null;
    }
}
