package ru.itmo.moona.cli.commands;

import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;

public class BatchListCommand implements Command {
    @Override
    public void execute(InputParser input) {
        try {
            if (input.getArg() == null || input.getArg().isBlank()) {
                throw new IllegalArgumentException("expected a reagent ID");
            } else {
                long id = Long.parseLong(input.getArg());
                if (input.getKey() != null && input.getKey().equalsIgnoreCase("--active")) {
                    StockManager.findActiveBatch(id);
                } else {
                    StockManager.findBatch(id);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("invalid id. expected a number");
        }
    }

    @Override
    public String getName() {
        return "batch_list";
    }

    @Override
    public String getDescription() {
        return "<reagent_id> displays a list of all batches (use --active to exclude archived ones)";
    }
}
