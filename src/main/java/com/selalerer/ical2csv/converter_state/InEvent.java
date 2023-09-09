package com.selalerer.ical2csv.converter_state;

import com.selalerer.ical2csv.model.CalendarEvent;
import com.selalerer.ical2csv.utils.ICalDateTimeParser;
import com.selalerer.ical2csv.utils.ICalUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
public class InEvent implements ConverterState {

    private final CalendarEvent event = new CalendarEvent();
    private final ConverterStateContext context;


    public InEvent(ConverterStateContext context) {
        this.context = context;
    }

    @Override
    public ConverterState process(String line) {
        log.debug("line={}", line);

        if ("END:VEVENT".equals(line)) {
			log.debug("{}", event);
			ICalUtils.splitToRepeatedEventsIfNecessary(event, context.getToTimeInZone())
                    .forEach(this::handleEvent);
            return new NotInEvent(context);
        }

        if (line.startsWith("DTSTART")) {
            event.setStartTime(parseDateTimeLine(line));
        } else if (line.startsWith("DTEND")) {
            event.setEndTime(parseDateTimeLine(line));
        } else if (line.startsWith("DESCRIPTION:")) {
            event.setDescription(ICalUtils.getValue(line));
        } else if (line.startsWith("LOCATION:")) {
            event.setLocation(ICalUtils.getValue(line));
        } else if (line.startsWith("STATUS:")) {
            event.setStatus(ICalUtils.getValue(line));
        } else if (line.startsWith("SUMMARY:")) {
            event.setSummary(ICalUtils.getValue(line));
        } else if (line.startsWith("RRULE:")) {
            event.setRepeatRule(ICalUtils.parseRepeatRule(ICalUtils.getValue(line), ZoneId.of("UTC"), context.getTimezone()));
        } else if (line.startsWith("EXDATE")) {
            event.addExceptDate(parseDateTimeLine(line));
        }
		
        return this;
    }

    private void handleEvent(CalendarEvent event) {
        log.debug("Event start time is not null ? {}", event.getStartTime() != null);
        if (event.getStartTime() != null) {
            log.debug("Time range {} ==> {}", context.getFromTimeInZone(), context.getToTimeInZone());
            if ((context.getFromTimeInZone() == null ||
                    !event.getStartTime().isBefore(context.getFromTimeInZone())) &&
                    (context.getToTimeInZone() == null ||
                            !event.getEndTime().isAfter(context.getToTimeInZone()))) {

                log.debug("Event is within defined time range.");
                context.getConsumer().accept(event);
            } else {
                log.debug("Event is not within defined time range.");
            }
        }
    }

    private LocalDateTime parseDateTimeLine(String line) {
        var fromTimezone = ICalUtils.getTimeZone(line, ZoneId.of("UTC"));
        var dateTimeString = ICalUtils.getValue(line);

        return ICalDateTimeParser.parse(dateTimeString, fromTimezone, context.getTimezone());
    }
}
