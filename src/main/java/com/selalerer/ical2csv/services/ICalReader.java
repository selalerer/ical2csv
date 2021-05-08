package com.selalerer.ical2csv.services;

import com.selalerer.ical2csv.converter_state.ConverterStateMachine;
import com.selalerer.ical2csv.model.CalendarEvent;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@Service
public class ICalReader {

    @SneakyThrows
    public void readAll(Path icalInput,
                        LocalDateTime fromTime, LocalDateTime toTime,
                        Consumer<CalendarEvent> consumer) {

        try (var lineStream = Files.lines(icalInput);) {

            var converter = new ConverterStateMachine(consumer, fromTime, toTime);

            var linesIterator = lineStream.iterator();
            while (linesIterator.hasNext()) {
                if (!converter.process(linesIterator.next())) {
                    break;
                }
            }
        }
    }

}
