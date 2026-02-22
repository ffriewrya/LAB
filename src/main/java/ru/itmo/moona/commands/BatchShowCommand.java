package ru.itmo.moona.commands;

import ru.itmo.moona.StockManager;
import ru.itmo.moona.commands.base.Command;
import ru.itmo.moona.commands.base.InputParser;

public class BatchShowCommand implements Command {
    @Override
    public void execute(InputParser input) {
        if (input.getArg().isBlank()) {
            throw new IllegalArgumentException("expected batch ID");
        } else {
            long id = Long.parseLong(input.getArg());
            StockManager.showBatch(id);
        }

    }

    @Override
    public String getName() {
        return "batch_show";
    }

    @Override
    public String getDescription() {
        return "<batch_id> displays a batch by its id";
    }
}
