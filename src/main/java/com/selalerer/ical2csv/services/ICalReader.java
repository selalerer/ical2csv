package com.selalerer.ical2csv.services;

import com.selalerer.ical2csv.converter_state.ConverterStateMachine;
import com.selalerer.ical2csv.model.CalendarEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j
@Service
public class ICalReader {

    public void readAll(Stream<String> lineStream,
                        Consumer<CalendarEvent> consumer,
                        Consumer<ZoneId> zoneIdConsumer) {

        var converter = new ConverterStateMachine(consumer, zoneIdConsumer);

        var lineNumber = 1;
        var linesIterator = lineStream.iterator();
        while (linesIterator.hasNext()) {
            try {
                var line = linesIterator.next();
                if (!converter.process(line)) {
                    break;
                }
            } catch (RuntimeException e) {
                log.error("Exception at line {}", lineNumber, e);
            }
            ++lineNumber;
        }
    }


}
