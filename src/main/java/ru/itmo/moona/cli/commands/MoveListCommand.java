package ru.itmo.moona.cli.commands;

import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;

public class MoveListCommand implements Command {
    @Override
    public void execute(InputParser input) {
        try {
            if (input.getArg() == null || input.getArg().isBlank()) {
                throw new IllegalArgumentException("expected batch ID");
            } else {
                long id = Long.parseLong(input.getArg());
                if (input.getKeyValue() == null || input.getKeyValue().isBlank()) {
                    StockManager.showMoves(id);
                } else {
                    int amount = Integer.parseInt(input.getKeyValue());
                    StockManager.showAmountOfMoves(id, amount);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("invalid id. expected a number");
        }
    }

    @Override
    public String getName() {
        return "move_list";
    }

    @Override
    public String getDescription() {
        return "<batch_id> displays all moves of the requested batch (use --last N for showing only the last N moves)";
    }
}
