package com.selalerer.ical2csv.services;

import com.selalerer.ical2csv.utils.ICalFileFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@Slf4j
@Service
public class GraphicInterface {

    static {
        // Must be first in the class (so AWT classes will be loaded only after this flag is set)
        System.setProperty("java.awt.headless", "false");
    }

    public void run() {

        var frame = new JFrame("ICalendar 2 CSV converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );

        var fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new ICalFileFilter());
        fileChooser.setApproveButtonText("Convert to CSV");
        fileChooser.addActionListener(new L1());

        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(fileChooser);
        frame.pack();

        frame.setVisible(true);
    }

    private static class L1 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            log.info("Button clicked :-)");
        }
    }
}
