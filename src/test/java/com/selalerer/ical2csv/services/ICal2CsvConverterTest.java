package com.selalerer.ical2csv.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

public class ICal2CsvConverterTest {

    private ICal2CsvConverter iCal2CsvConverter;

    @BeforeEach
    public void init() {
        iCal2CsvConverter = new ICal2CsvConverter(new ICalReader());
    }

    @Test
    @SneakyThrows
    public void recurring() {
        var tempFolder = Files.createTempDirectory("");
        var tempFile = Files.createTempFile(tempFolder, "", ".csv");
        try {
            try (var s = getClass().getResourceAsStream("/one_recurring.ical")) {
                iCal2CsvConverter.convert(s,
                        LocalDateTime.of(2022, 12, 1, 0, 0),
                        LocalDateTime.of(2022, 12, 31, 0, 0),
                        8, 21, tempFile);
            }

            Files.list(tempFolder).forEach(p -> {
                System.out.println("====================================================");
                System.out.println("File " + p);
                System.out.println("====================================================");
                try {
                    System.out.println(Files.readString(p));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("====================================================");
            });
        } finally {
            System.out.println("Deleting folder: " + tempFolder);
            FileSystemUtils.deleteRecursively(tempFolder);
        }
    }

}