package ru.itmo.moona.commands;

import ru.itmo.moona.StockManager;
import ru.itmo.moona.commands.base.Command;
import ru.itmo.moona.commands.base.InputParser;

public class BatchListCommand implements Command {
    @Override
    public void execute(InputParser input) {
        try {
            if (input.getArg().isBlank()) {
                throw new IllegalArgumentException("expected a reagent ID");
            } else {
                long id = Long.parseLong(input.getArg());
                if (input.getKeyValue().equalsIgnoreCase("active")) {
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
