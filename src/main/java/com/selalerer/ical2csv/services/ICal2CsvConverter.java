package com.selalerer.ical2csv.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class ICal2CsvConverter {

    private final ICalReader reader;

    public ICal2CsvConverter(ICalReader reader) {
        this.reader = reader;
    }

    public void convert(Path icalFile, LocalDateTime fromTime, LocalDateTime toTime,
                        int fromHour, int toHour, Path csvFile) {

        var builder = new CalendarTable();

        reader.readAll(icalFile, fromTime, toTime, builder);

        builder.toCsv(csvFile, fromHour, toHour);
    }
}
