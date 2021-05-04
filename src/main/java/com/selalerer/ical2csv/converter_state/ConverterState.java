package com.selalerer.ical2csv.converter_state;

public interface ConverterState {
    ConverterState process(String line);
}
