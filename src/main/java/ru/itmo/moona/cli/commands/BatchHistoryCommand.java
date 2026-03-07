package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;
import ru.itmo.moona.domain.ReagentBatch;
import ru.itmo.moona.service.StockManager;

public class BatchHistoryCommand implements Command {
    private final StockManager manager;

    public BatchHistoryCommand(StockManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(InputParser input) {
        try {
            if (input.getArg() == null || input.getArg().isBlank()) {
                throw new IllegalArgumentException("expected a batch ID.");
            }
            long id = Long.parseLong(input.getArg());
            ReagentBatch batch = manager.getBatch(id);
            manager.printHistory(batch);
        } catch (NumberFormatException e) {
            System.err.println("invalid id. expected a number");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "batch_history";
    }

    @Override
    public String getDescription() {
        return "<batch_id> displays all versions of the batch.";
    }
}
