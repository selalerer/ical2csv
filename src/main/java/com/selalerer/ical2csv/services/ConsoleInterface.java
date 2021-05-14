package com.selalerer.ical2csv.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ConsoleInterface {

    private final ICalReader convertingService;

    public ConsoleInterface(ICalReader convertingService) {
        this.convertingService = convertingService;
    }

    public void run(Path icalFile) throws IOException {

        var inputFileAsString = icalFile.toString();

        var outputFile = Path.of(FilenameUtils.getPath(inputFileAsString) +
                FilenameUtils.getBaseName(inputFileAsString) + ".csv");

        var builder = new CalendarTableBuilder();

        var fromTimeStr = System.getProperty("fromTime", LocalDateTime.MIN.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        var toTimeStr = System.getProperty("toTime", LocalDateTime.MAX.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        var fromTime = LocalDateTime.parse(fromTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        var toTime = LocalDateTime.parse(toTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        var fromHour = Integer.parseInt(System.getProperty("fromHour", "8"));
        var toHour = Integer.parseInt(System.getProperty("toHour", "21"));

        convertingService.readAll(icalFile, fromTime, toTime, builder);

        builder.toCsv(outputFile, fromHour, toHour);
    }
}
