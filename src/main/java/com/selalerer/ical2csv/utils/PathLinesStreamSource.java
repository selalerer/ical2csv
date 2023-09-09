package com.selalerer.ical2csv.utils;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class PathLinesStreamSource implements LinesStreamSource {
    private final Path path;
    @Override
    @SneakyThrows
    public Stream<String> lines() {
        return Files.lines(path);
    }
}
