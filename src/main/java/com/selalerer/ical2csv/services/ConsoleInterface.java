package com.selalerer.ical2csv.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class ConsoleInterface {

    private final ICal2CsvConvertingService convertingService;

    public ConsoleInterface(ICal2CsvConvertingService convertingService) {
        this.convertingService = convertingService;
    }

    public void run(Path icalFile) {

        var inputFileAsString = icalFile.toString();

        var outputFile = Path.of(FilenameUtils.getPath(inputFileAsString) +
                FilenameUtils.getBaseName(inputFileAsString) + ".csv");

        convertingService.convert(icalFile, outputFile, LocalDateTime.MIN, LocalDateTime.MAX);
    }
}
