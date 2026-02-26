package ru.itmo.moona.commands;

import ru.itmo.moona.StockManager;
import ru.itmo.moona.commands.base.Command;
import ru.itmo.moona.commands.base.InputParser;

public class StockReportCommand implements Command {
    @Override
    public void execute(InputParser input) {
        if (input.getArg().isBlank()) {
            StockManager.stockReport();
        } else {
            StockManager.stockReport(input.getArg());
        }
    }

    @Override
    public String getName() {
        return "stock_report";
    }

    @Override
    public String getDescription() {
        return "displays all of the reagents, batches and moves. use --expires-before to see batches that are expiring soon.";
    }
}
