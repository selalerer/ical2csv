package com.selalerer.ical2csv.services;

import com.selalerer.ical2csv.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

@Slf4j
@Service
public class GraphicInterface {

    static {
        // Must be first in the class (so AWT classes will be loaded only after this flag is set)
        System.setProperty("java.awt.headless", "false");
    }

    private final PropertiesProvider propertiesProvider;
    private final ICal2CsvConverter converter;

    public GraphicInterface(ICal2CsvConverter converter, PropertiesProvider propertiesProvider) {
        this.converter = converter;
        this.propertiesProvider = propertiesProvider;
    }

    public void run() {

        var frame = new JFrame("ICalendar 2 CSV converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );

        var selectedFileLabel = new JLabel("");
        var convertFileButton = new JButton("Convert");
        var selectFileButton = new JButton("Choose ICalendar file");
        var statusLabel = new JLabel("");

        selectFileButton.addActionListener(new OnSelectFileButtonPressed(frame.getContentPane(),
                selectedFileLabel,
                statusLabel));

        convertFileButton.addActionListener(new OnConvertButtonPressed(selectedFileLabel, statusLabel));

        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(selectFileButton);
        frame.getContentPane().add(selectedFileLabel);
        frame.getContentPane().add(convertFileButton);
        frame.getContentPane().add(statusLabel);

        frame.setVisible(true);
    }

    private static class OnSelectFileButtonPressed implements ActionListener {

        private final Component parent;
        private final JLabel selectedFileLabel;
        private final JLabel statusLabel;

        public OnSelectFileButtonPressed(Component parent, JLabel selectedFileLabel,
                                         JLabel statusLabel) {
            this.parent = parent;
            this.selectedFileLabel = selectedFileLabel;
            this.statusLabel = statusLabel;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            statusLabel.setText("");
            selectedFileLabel.setText("");

            var fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("ICalendar files", "ics"));

            var result = fileChooser.showOpenDialog(parent);

            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFileLabel.setText(fileChooser.getSelectedFile().toString());
            }
        }
    }

    private class OnConvertButtonPressed implements ActionListener {

        private final JLabel selectedFileLabel;
        private final JLabel statusLabel;

        public OnConvertButtonPressed(JLabel selectedFileLabel, JLabel statusLabel) {
            this.selectedFileLabel = selectedFileLabel;
            this.statusLabel = statusLabel;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                var icalFile = Path.of(selectedFileLabel.getText());
                var csvFile = FileUtils.replaceExtension(icalFile, "csv");

                statusLabel.setText("Processing...");

                new Thread( () -> {
                    converter.convert(icalFile, propertiesProvider.getFromTime(), propertiesProvider.getToTime(),
                            propertiesProvider.getFromHour(), propertiesProvider.getToHour(),
                            csvFile);

                    statusLabel.setText("Finished creating CSV files");
                }).start();

            } catch (Exception e) {
                Throwable t = e;
                while (t.getCause() != null && t.getCause() != t) {
                    t = t.getCause();
                }
                statusLabel.setText("ERROR: " + t.getClass().getSimpleName() + ": " + t.getMessage());
                log.error("Exception while converting ics to csv", e);
            }
        }
    }
}
