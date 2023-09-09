package com.selalerer.ical2csv.utils;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class InputStreamLinesStreamSource implements LinesStreamSource{
    private final InputStream inputStream;
    @Override
    public Stream<String> lines() {
        return new BufferedReader(new InputStreamReader(inputStream)).lines();
    }
}

