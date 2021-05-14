package com.selalerer.ical2csv.services;

import com.selalerer.ical2csv.utils.ICalFileFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
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

        var selectFileButton = new JButton("Choose ICalendar file");
        selectFileButton.addActionListener(new OnSelectFileButtonPressed(frame.getContentPane()));

        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(selectFileButton);
        //frame.pack();

        frame.setVisible(true);
    }

    private static class OnSelectFileButtonPressed implements ActionListener {

        private final Component parent;

        public OnSelectFileButtonPressed(Component parent) {
            this.parent = parent;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            var fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("ICalendar files", "ics"));
            fileChooser.setApproveButtonText("Convert to CSV");
            fileChooser.addActionListener(new L1());

            var result = fileChooser.showOpenDialog(parent);

            if (result == JFileChooser.APPROVE_OPTION) {
                log.info("File chosen: {}", fileChooser.getSelectedFile());
            }
        }
    }

    private static class L1 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            log.info("Button clicked :-)");
        }
    }
}
