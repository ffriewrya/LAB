package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.CommandManager;
import ru.itmo.moona.cli.base.InputParser;

public class RedoCommand implements Command {
    private final CommandManager manager;

    public RedoCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(InputParser input) {
        manager.redo();
    }

    @Override
    public String getName() {
        return "redo";
    }

    @Override
    public String getDescription() {
        return "";
    }
}
