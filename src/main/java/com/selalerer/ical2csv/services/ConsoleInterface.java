package com.selalerer.ical2csv.services;

import com.selalerer.ical2csv.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class ConsoleInterface {

    private final PropertiesProvider propertiesProvider;
    private final ICal2CsvConverter converter;

    public ConsoleInterface(ICal2CsvConverter converter, PropertiesProvider propertiesProvider) {
        this.converter = converter;
        this.propertiesProvider = propertiesProvider;
    }

    public void run(Path icalFile) throws IOException {

        var outputFile = FileUtils.replaceExtension(icalFile, "csv");

        converter.convert(icalFile, propertiesProvider.getFromTime(),
                propertiesProvider.getToTime(), propertiesProvider.getFromHour(),
                propertiesProvider.getToHour(), outputFile);
    }
}
