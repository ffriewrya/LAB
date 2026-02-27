package ru.itmo.moona.cli.commands;

import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;

public class BatchShowCommand implements Command {
    @Override
    public void execute(InputParser input) {
        try {
            if (input.getArg() == null || input.getArg().isBlank()) {
                throw new IllegalArgumentException("expected batch ID");
            } else {
                long id = Long.parseLong(input.getArg());
                StockManager.showBatch(id);
            }
        } catch (NumberFormatException e) {
            System.err.println("invalid id. expected a number");
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
