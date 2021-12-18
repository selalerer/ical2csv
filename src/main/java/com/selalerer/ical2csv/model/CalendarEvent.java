package com.selalerer.ical2csv.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class CalendarEvent {
    private CalendarEvent sourceEvent;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String summary;
    private String location;
    private String status;
    private String description;
    private RepeatRule repeatRule;
    private List<LocalDateTime> exceptDates;

    public void addExceptDate(LocalDateTime exceptDate) {
        if (exceptDates == null) {
            exceptDates = new ArrayList<LocalDateTime>();
        }
        exceptDates.add(exceptDate);
    }

    public String[] toStringArray() {
        var a = new String[6];
        a[0] = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace('T', ' ');
        a[1] = endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace('T', ' ');
        a[2] = summary;
        a[3] = location;
        a[4] = status;
        a[5] = description;
        return a;
    }
}
