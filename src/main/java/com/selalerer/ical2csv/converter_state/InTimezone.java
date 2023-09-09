package com.selalerer.ical2csv.converter_state;

import com.selalerer.ical2csv.utils.ICalUtils;

import java.time.ZoneId;

public class InTimezone implements ConverterState {

    private final ConverterStateContext context;

    public InTimezone(ConverterStateContext context) {
        this.context = context;
    }

    @Override
    public ConverterState process(String line) {

        if ("END:VTIMEZONE".equals(line)) {
            return new NotInEvent(context);
        }

        if (line.startsWith("TZID:")) {
            context.setTimezone(ZoneId.of(ICalUtils.getValue(line)));
            if (context.getZoneIdConsumer() != null) {
                context.getZoneIdConsumer().accept(context.getTimezone());
            }
        }

        return this;
    }
}
