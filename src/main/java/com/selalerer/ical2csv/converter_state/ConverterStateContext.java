package com.selalerer.ical2csv.converter_state;

import com.selalerer.ical2csv.model.CalendarEvent;
import com.selalerer.ical2csv.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.DateTimeException;
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

    public LocalDateTime getFromTimeInZone() {
        return DateTimeUtils.migrateTimezone(getFromTime(), ZoneId.systemDefault(), getTimezone());
    }

    public LocalDateTime getToTimeInZone() {
        return DateTimeUtils.migrateTimezone(getToTime(), ZoneId.systemDefault(), getTimezone());
    }
}
