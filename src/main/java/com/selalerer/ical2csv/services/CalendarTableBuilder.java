package com.selalerer.ical2csv.services;

import com.opencsv.CSVWriter;
import com.selalerer.ical2csv.model.CalendarEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
@Service
public class CalendarTableBuilder implements Consumer<CalendarEvent> {

    private final Map<LocalDateTime, String> timeSlotToSummaryMap = new HashMap<>();

    private LocalDateTime minTimeSlot = LocalDateTime.MAX;
    private LocalDateTime maxTimeSlot = LocalDateTime.MIN;

    private final Set<LocalDate> datesWithEvents = new HashSet<>();

    @Override
    public void accept(CalendarEvent calendarEvent) {

        var timeSlot = calculateTimeSlot(calendarEvent.getStartTime());

        add(timeSlot, calendarEvent.getSummary());
        timeSlot = timeSlot.plusMinutes(30);
        while (timeSlot.isBefore(calendarEvent.getEndTime())) {
            add(timeSlot, calendarEvent.getSummary());
            timeSlot = timeSlot.plusMinutes(30);
        }
    }

    public void toCsv(Path csvFile, int fromHour, int toHour) throws IOException {

        var fromDate = minTimeSlot.toLocalDate();
        var toDate = maxTimeSlot.toLocalDate();

        var firstMonth = LocalDate.of(fromDate.getYear(), fromDate.getMonth(), 1);
        var lastMonth = LocalDate.of(toDate.getYear(), toDate.getMonth(), 1);

        var month = firstMonth;
        while (!month.isAfter(lastMonth)) {

            fromDate = month;
            toDate = month.plusMonths(1).minusDays(1);

            Path monthFile = createMonthFilename(csvFile, month);
            log.info("Writing file {}", monthFile);

            try (var csvWriter = new CSVWriter(new OutputStreamWriter(Files.newOutputStream(monthFile)));) {
                writeCsvHeader(csvWriter, fromDate, toDate);
                writeCsvLines(csvWriter, fromDate, toDate, fromHour, toHour);
            }

            month = month.plusMonths(1);
        }
    }

    private Path createMonthFilename(Path csvFile, LocalDate month) {
        var origFilename = csvFile.toString();
        return Path.of(FilenameUtils.getPath(origFilename) +
                FilenameUtils.getBaseName(origFilename) +
                String.format("_%04d_%02d", month.getYear(), month.getMonthValue()) +
                ".csv");
    }

    private void writeCsvLines(CSVWriter csvWriter, LocalDate fromDate, LocalDate toDate,
                               int startHour, int endHour) {

        var startTimeSlot = LocalTime.of(startHour, 0);
        var endTimeSlot = LocalTime.of(endHour, 0);
        var timeSlot = startTimeSlot;

        do {
            writeCsvLine(csvWriter, timeSlot, fromDate, toDate);

            if (timeSlot.equals(endTimeSlot)) {
                break;
            }

            timeSlot = timeSlot.plusMinutes(30);

            // The LocalTime loops back to 00:00
        } while (!timeSlot.equals(startTimeSlot));
    }

    private void writeCsvLine(CSVWriter csvWriter, LocalTime timeSlot, LocalDate fromDate, LocalDate toDate) {

        var line = new ArrayList<String>();

        line.add(timeSlot.format(DateTimeFormatter.ISO_LOCAL_TIME));

        var date = fromDate;

        while (!date.isAfter(toDate)) {

            if (datesWithEvents.contains(date)) {
                var fullTimeSlot = LocalDateTime.of(date, timeSlot);

                if (timeSlotToSummaryMap.containsKey(fullTimeSlot)) {
                    line.add(timeSlotToSummaryMap.get(fullTimeSlot));
                } else {
                    line.add("");
                }
            }

            date = date.plusDays(1);
        }

        csvWriter.writeNext(line.toArray(new String[0]));
    }

    private int writeCsvHeader(CSVWriter csvWriter, LocalDate fromDate, LocalDate toDate) {

        var headers = new ArrayList<String>();
        // First column is for the hours:minutes of the timeslots
        headers.add("");

        var date = fromDate;

        while (!date.isAfter(toDate)) {
            if (datesWithEvents.contains(date)) {
                headers.add(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
            date = date.plusDays(1);
        }

        csvWriter.writeNext(headers.toArray(new String[0]));

        return headers.size();
    }

    private void add(LocalDateTime timeSlot, String summary) {

        timeSlotToSummaryMap.put(timeSlot, summary);
        datesWithEvents.add(timeSlot.toLocalDate());

        if (timeSlot.isBefore(minTimeSlot)) {
            minTimeSlot = timeSlot;
        }

        if (timeSlot.isAfter(maxTimeSlot)) {
            maxTimeSlot = timeSlot;
        }
    }

    public LocalDateTime calculateTimeSlot(LocalDateTime eventStartTime) {

        var minute = eventStartTime.getMinute();
        if (minute <= 15) {
            return LocalDateTime.of(eventStartTime.toLocalDate(),
                    LocalTime.of(eventStartTime.getHour(), 0));
        };

        if (minute >= 16 && minute <= 45) {
            return LocalDateTime.of(eventStartTime.toLocalDate(),
                    LocalTime.of(eventStartTime.getHour(), 30));
        }

        // minute >= 46
        var hour = eventStartTime.getHour();
        ++hour;
        if (hour > 23) {
            return LocalDateTime.of(eventStartTime.toLocalDate().plusDays(1),
                    LocalTime.of(0, 0));
        }

        return LocalDateTime.of(eventStartTime.toLocalDate(),
                LocalTime.of(hour, 0));
    }
}
