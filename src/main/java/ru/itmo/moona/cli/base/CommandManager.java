package ru.itmo.moona.cli.base;

import java.util.HashMap;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();

    public void create(Command command) {
        commands.put(command.getName(), command);
    }

    public void start(String rawInput) {
        InputParser input = new InputParser(rawInput);
        Command cmd = commands.get(input.getCommandName());

        if (cmd == null) {
            throw new IllegalArgumentException("this command doesn't exist");
        } else {
            cmd.execute(input);
        }
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }
}
