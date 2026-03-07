package ru.itmo.moona.cli.commands;

import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;

public class ReagentListCommand implements Command {
    private final StockManager manager;

    public ReagentListCommand(StockManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(InputParser input) {
        if (input.getKeyValue() == null || input.getKeyValue().isBlank()) {
            manager.printReagents();
        } else {
            if (input.getKeyValue().length() > 64) {
                throw new IllegalArgumentException("query exceeds maximum length of 64 characters");
            } else {
                manager.findReagent(input.getArg());
            }
        }
    }

    @Override
    public String getName() {
        return "reag_list";
    }

    @Override
    public String getDescription() {
        return "displays a list of all reagents (use -qq to search by name)";
    }
}
