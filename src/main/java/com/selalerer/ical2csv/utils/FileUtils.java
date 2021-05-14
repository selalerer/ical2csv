package com.selalerer.ical2csv.utils;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;

public class FileUtils {

    public static Path replaceExtension(Path file, String newExtension) {
        var fileAsString = file.toString();

        return Path.of(FilenameUtils.getFullPath(fileAsString) +
                FilenameUtils.getBaseName(fileAsString) + "." + newExtension);
    }

    public static Path addSuffix(Path file, String suffix) {

        var fileAsString = file.toString();

        return Path.of(FilenameUtils.getFullPath(fileAsString) +
                FilenameUtils.getBaseName(fileAsString) + suffix + "." +
                FilenameUtils.getExtension(fileAsString));
    }


}
