package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;

public class ExitCommand implements Command {
    @Override
    public void execute(InputParser input) {
        System.out.println("exiting stock.");
        System.exit(0);
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "exits the application.";
    }
}
