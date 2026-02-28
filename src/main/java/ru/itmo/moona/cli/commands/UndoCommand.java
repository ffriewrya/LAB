package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.CommandManager;
import ru.itmo.moona.cli.base.InputParser;

public class UndoCommand implements Command {
    @Override
    public void execute(InputParser input) {
        CommandManager.undo();
    }

    @Override
    public String getName() {
        return "undo";
    }

    @Override
    public String getDescription() {
        return "";
    }
}
