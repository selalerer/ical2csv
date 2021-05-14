package com.selalerer.ical2csv.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Slf4j
@Service
public class ApplicationInterfaceSelector implements CommandLineRunner {

    private final ConsoleInterface consoleInterface;
    private final GraphicInterface graphicInterface;

    public ApplicationInterfaceSelector(ConsoleInterface consoleInterface, GraphicInterface graphicInterface) {
        this.consoleInterface = consoleInterface;
        this.graphicInterface = graphicInterface;
    }

    @Override
    public void run(String... args) throws Exception {

        if (args.length > 0) {
            consoleInterface.run(Path.of(args[0]));
        } else {
            graphicInterface.run();
        }
    }
}
