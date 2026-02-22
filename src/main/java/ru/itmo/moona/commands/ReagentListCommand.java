package ru.itmo.moona.commands;

import ru.itmo.moona.StockManager;
import ru.itmo.moona.commands.base.Command;
import ru.itmo.moona.commands.base.InputParser;

public class ReagentListCommand implements Command {
    @Override
    public void execute(InputParser input) {
        if (input.getKeyValue() == null) {
            StockManager.printReagents();
        } else {
            if (input.getArg().length() > 64) {
                throw new IllegalArgumentException("query exceeds maximum length of 64 characters");
            } else {
                StockManager.findReagent(input.getArg());
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
