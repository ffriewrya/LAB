package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.CommandManager;
import ru.itmo.moona.cli.base.InputParser;

public class RedoCommand implements Command {
    @Override
    public void execute(InputParser input) {
        CommandManager.redo();
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
