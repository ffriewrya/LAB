package ru.itmo.moona.commands;

import ru.itmo.moona.StockManager;
import ru.itmo.moona.commands.base.Command;
import ru.itmo.moona.commands.base.InputParser;

public class MoveListCommand implements Command {
    @Override
    public void execute(InputParser input) {
        if (input.getArg().isBlank()) {
            throw new IllegalArgumentException("expected batch ID");
        } else {
            long id = Long.parseLong(input.getArg());
            if (input.getKeyValue().isBlank()) {
                StockManager.showMoves(id);
            } else {
                int amount = Integer.parseInt(input.getKeyValue());
                StockManager.showAmountOfMoves(id, amount);
            }
        }

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }
}
