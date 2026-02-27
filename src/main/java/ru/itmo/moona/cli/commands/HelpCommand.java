package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;

import java.util.Map;

public class HelpCommand implements Command {
    private final Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(InputParser input) {
        System.out.println("available commands");
        for (Command cmd : commands.values()) {
            System.out.printf("%-15s : %s%n", cmd.getName(), cmd.getDescription());
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "displays a list of all commands";
    }
}
