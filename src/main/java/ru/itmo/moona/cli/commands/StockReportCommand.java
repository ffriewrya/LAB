package ru.itmo.moona.cli.commands;

import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;

public class StockReportCommand implements Command {
    @Override
    public void execute(InputParser input) {
        if (input.getKey() == null || input.getKey().isBlank()) {
            StockManager.stockReport();
        } else {
            StockManager.stockReport(input.getKeyValue());
        }
    }

    @Override
    public String getName() {
        return "stock_report";
    }

    @Override
    public String getDescription() {
        return "displays all of the reagents, batches and moves (use --expires-before to see batches that are expiring soon).";
    }
}
