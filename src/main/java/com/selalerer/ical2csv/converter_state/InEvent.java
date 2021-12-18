package com.selalerer.ical2csv.converter_state;

import com.selalerer.ical2csv.model.CalendarEvent;
import com.selalerer.ical2csv.utils.ICalDateTimeParser;
import com.selalerer.ical2csv.utils.ICalUtils;
import lombok.extern.slf4j.Slf4j;

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
            if (event.getStartTime() != null) {
                if (!event.getStartTime().isBefore(context.getFromTimeInZone()) &&
                    !event.getEndTime().isAfter(context.getToTimeInZone())) {

                    context.getConsumer().accept(event);
                }
            }
            return new NotInEvent(context);
        }

        if (line.startsWith("DTSTART:")) {
            event.setStartTime(dateTimeParser.parse(ICalUtils.getValue(line), context.getTimezone()));
        } else if (line.startsWith("DTEND:")) {
            event.setEndTime(dateTimeParser.parse(ICalUtils.getValue(line), context.getTimezone()));
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
}
