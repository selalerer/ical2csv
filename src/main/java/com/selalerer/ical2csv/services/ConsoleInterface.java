package com.selalerer.ical2csv.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

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

        convertingService.readAll(icalFile, LocalDateTime.MIN, LocalDateTime.MAX, builder);

        builder.toCsv(outputFile);
    }
}
