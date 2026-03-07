package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;
import ru.itmo.moona.cli.base.Undoable;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.storage.FileStorage;

public class SaveCommand implements Command, Undoable {
    private final StockManager manager;
    private final FileStorage storage;

    public SaveCommand(StockManager manager, FileStorage storage) {
        this.manager = manager;
        this.storage = storage;
    }

    @Override
    public void execute(InputParser input) {
        if (input.getArg() == null || input.getArg().isBlank()) {
            throw new IllegalArgumentException("expected a file path");
        }
        storage.saveToJson(input.getArg());
        System.out.println("stock successfully saved to " + input.getArg());
    }

    @Override
    public String getName() {
        return "save_to";
    }

    @Override
    public String getDescription() {
        return "<file_path> exports current stock state to JSON";
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
