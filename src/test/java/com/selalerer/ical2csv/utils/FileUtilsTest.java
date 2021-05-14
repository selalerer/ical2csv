package com.selalerer.ical2csv.utils;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileUtilsTest {

    @Test
    public void replaceExtension_absolutePath() {

        var input = Path.of("/home/sela/software_projects/ical2csv/example/nilieisenberg@gmail.com.ics");
        var expected = Path.of("/home/sela/software_projects/ical2csv/example/nilieisenberg@gmail.com.csv");

        var result = FileUtils.replaceExtension(input, "csv");

        assertEquals(expected, result);
    }

    @Test
    public void replaceExtension_relativePath() {

        var input = Path.of("../example/nilieisenberg@gmail.com.ics");
        var expected = Path.of("../example/nilieisenberg@gmail.com.csv");

        var result = FileUtils.replaceExtension(input, "csv");

        assertEquals(expected, result);
    }

    @Test
    public void addSuffix_relativePath() {

        var input = Path.of("../example/nilieisenberg@gmail.com.csv");
        var expected = Path.of("../example/nilieisenberg@gmail.com_2014_03.csv");

        var result = FileUtils.addSuffix(input, "_2014_03");

        assertEquals(expected, result);
    }

    @Test
    public void addSuffix_absolutePath() {

        var input = Path.of("/home/sela/software_projects/ical2csv/example/nilieisenberg@gmail.com.csv");
        var expected = Path.of("/home/sela/software_projects/ical2csv/example/nilieisenberg@gmail.com_2016_01.csv");

        var result = FileUtils.addSuffix(input, "_2016_01");

        assertEquals(expected, result);
    }

}