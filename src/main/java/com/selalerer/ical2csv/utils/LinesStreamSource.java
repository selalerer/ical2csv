package com.selalerer.ical2csv.utils;

import java.util.stream.Stream;

public interface LinesStreamSource {
    Stream<String> lines();
}
