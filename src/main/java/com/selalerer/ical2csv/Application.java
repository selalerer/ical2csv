package com.selalerer.ical2csv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
@SpringBootApplication
public class Application {

    static {
        // Must be first in the class (so AWT classes will be loaded only after this flag is set)
        System.setProperty("java.awt.headless", "false");
    }

    public static void main(String [] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//
//        JFrame frame = new JFrame("My First GUI");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(300,300);
//        JButton button = new JButton("Convert");
//        frame.getContentPane().add(button); // Adds Button to content pane of frame
//        button.addActionListener(new L1());
//        frame.setVisible(true);
//    }
//
//    private static class L1 implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent actionEvent) {
//            log.info("Button clicked :-)");
//        }
//    }
}
