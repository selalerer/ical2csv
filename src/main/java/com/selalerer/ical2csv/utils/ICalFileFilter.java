package com.selalerer.ical2csv.utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ICalFileFilter extends FileFilter {
    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getPath().endsWith(".ics");
    }

    @Override
    public String getDescription() {
        return "ICalendar files";
    }
}
