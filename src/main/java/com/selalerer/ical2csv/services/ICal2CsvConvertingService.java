package com.selalerer.ical2csv.services;

import com.opencsv.CSVWriter;
import com.selalerer.ical2csv.converter_state.ConverterStateMachine;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ICal2CsvConvertingService {

    @SneakyThrows
    public void convert(Path icalInput, Path csvOutput, LocalDateTime fromTime, LocalDateTime toTime) {

        try (var lineStream = Files.lines(icalInput);
             var csvWriter = new CSVWriter(new OutputStreamWriter(Files.newOutputStream(csvOutput)));) {

            csvWriter.writeNext(List.of("Start Time", "End Time", "Summary", "Location", "Status", "Description")
                    .toArray(new String[5]));

            var converter = new ConverterStateMachine(csvWriter, fromTime, toTime);

            var linesIterator = lineStream.iterator();
            while (linesIterator.hasNext()) {
                if (!converter.process(linesIterator.next())) {
                    break;
                }
            }
        }
    }

}
