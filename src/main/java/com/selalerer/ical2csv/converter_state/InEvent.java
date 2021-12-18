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
    private final ICalDateTimeParser dateTimeParser = new ICalDateTimeParser();
    private final ConverterStateContext context;

    public InEvent(ConverterStateContext context) {
        this.context = context;
    }

    @Override
    public ConverterState process(String line) {
        log.debug("line={}", line);

        if ("END:VEVENT".equals(line)) {
			log.debug("{}", event);
			log.debug("event start time is not null ? {}", event.getStartTime() != null);
            if (event.getStartTime() != null) {
				log.debug("Time range {} ==> {}", context.getFromTimeInZone(), context.getToTimeInZone());
                if (!event.getStartTime().isBefore(context.getFromTimeInZone()) &&
                    !event.getEndTime().isAfter(context.getToTimeInZone())) {

					log.debug("Event is within defined time range.");
                    context.getConsumer().accept(event);
                } else {
					log.debug("Event is not within defined time range.");
				}
            }
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
        }
		
        return this;
    }

    private LocalDateTime parseDateTimeLine(String line) {
        var fromTimezone = ICalUtils.getTimeZone(line, ZoneId.of("UTC"));
        var dateTimeString = ICalUtils.getValue(line);

        return dateTimeParser.parse(dateTimeString, fromTimezone, context.getTimezone());
    }
}
