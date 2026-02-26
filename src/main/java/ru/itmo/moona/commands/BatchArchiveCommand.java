package ru.itmo.moona.commands;

import ru.itmo.moona.StockManager;
import ru.itmo.moona.commands.base.Command;
import ru.itmo.moona.commands.base.InputParser;

public class BatchArchiveCommand implements Command {
    @Override
    public void execute(InputParser input) {
        try {
            if (input.getArg().isBlank()) {
                throw new IllegalArgumentException("expected a batch ID");
            } else {
                long id = Long.parseLong(input.getArg());
                StockManager.archiveBatch(id);
            }
        } catch (NumberFormatException e) {
            System.err.println("invalid id. expected a number");
        }
    }

    @Override
    public String getName() {
        return "batch_archive";
    }

    @Override
    public String getDescription() {
        return "<batch_id> sets batches status to ARCHIVED.";
    }
}
