package com.selalerer.ical2csv.converter_state;

import com.selalerer.ical2csv.model.CalendarEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Consumer;

@Data
@AllArgsConstructor(staticName = "of")
public class ConverterStateContext {
    private Consumer<CalendarEvent> consumer;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private ZoneId timezone;
}
