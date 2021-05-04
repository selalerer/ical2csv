package com.selalerer.ical2csv.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Slf4j
@Service
public class ApplicationInterfaceSelector implements CommandLineRunner {

    private final ConsoleInterface consoleInterface;

    public ApplicationInterfaceSelector(ConsoleInterface consoleInterface) {
        this.consoleInterface = consoleInterface;
    }

    @Override
    public void run(String... args) throws Exception {

        if (args.length > 0) {
            consoleInterface.run(Path.of(args[0]));
        } else {
            // TODO
            log.error("Please provide file name (currently only supporting console interface)");
        }
    }
}
