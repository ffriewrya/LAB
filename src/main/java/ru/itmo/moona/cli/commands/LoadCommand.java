package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;
import ru.itmo.moona.cli.base.Undoable;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.storage.FileStorage;
import ru.itmo.moona.storage.StockSnapshot;
import ru.itmo.moona.storage.StockValidator;

public class LoadCommand implements Command, Undoable {
    private final StockManager manager;
    private final FileStorage storage;
    private final StockValidator validator;

    public LoadCommand(StockManager manager, FileStorage storage, StockValidator validator) {
        this.manager = manager;
        this.storage = storage;
        this.validator = validator;
    }

    @Override
    public void execute(InputParser input) {
        if (input.getArg() == null || input.getArg().isBlank()) {
            throw new IllegalArgumentException("expected a file path");
        }
        StockSnapshot snapshot = storage.loadFromJson(input.getArg());
        validator.validate(snapshot);
        manager.loadStock(snapshot);
        System.out.println("successfully updated stock to " + input.getArg());
    }

    @Override
    public String getName() {
        return "load_from";
    }

    @Override
    public String getDescription() {
        return "<file_path> updates current stock from JSON";
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
