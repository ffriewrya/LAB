package ru.itmo.moona.commands;

import ru.itmo.moona.StockManager;
import ru.itmo.moona.StockMove;
import ru.itmo.moona.StockMoveType;
import ru.itmo.moona.commands.base.Command;
import ru.itmo.moona.commands.base.InputParser;

import java.util.Scanner;

public class AddMoveCommand implements Command {
    private final Scanner scanner;

    public AddMoveCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(InputParser input) {
        if (input.getArg().isBlank()) {
            throw new IllegalArgumentException("expected a batch ID");
        } else {
            StockMove.MoveBuilder builder = new StockMove.MoveBuilder();
            builder.setId(StockManager.genMoveId());
            builder.setCreatedAt();
            builder.setOwnerUsername("SYSTEM");

            while (true) {
                try {
                    System.out.println("enter existing batch id");
                    String idInput = input.getArg();
                    long id = Long.parseLong(idInput);
                    if (StockManager.isBatchArcived(id)) {
                        throw new IllegalArgumentException("you can't move an archived batch");
                    } else {
                        builder.setBatchId(id);
                        builder.setUnit();
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("invalid id. expected a number");
                }
            }

            while (true) {
                try {
                    System.out.println("enter type");
                    String type = scanner.nextLine();
                    builder.setType(type);
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }

            while (true) {
                try {
                    System.out.println("enter quantity");
                    try {
                        String quantityInput = scanner.nextLine();
                        double quantity = Double.parseDouble(quantityInput);
                        builder.setQuantity(quantity);
                        break;
                    } catch (NumberFormatException e) {
                        System.err.println("invalid quantity. expected a number");
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }

            while (true) {
                try {
                    System.out.println("enter reason");
                    String reason = scanner.nextLine();
                    builder.setReason(reason);
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }

            while (true) {
                try {
                    System.out.println("enter moving date");
                    String date = scanner.nextLine();
                    if (date.isBlank()) {
                        builder.setMovedAt();
                    } else {
                        builder.setMovedAt(date);
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
            StockMove move = builder.build();
            StockManager.addMove(move);
            try {
                if (move.getType() == StockMoveType.IN) {
                    StockManager.moveIn(move);
                } else {
                    StockManager.moveOutDiscard(move);
                }
                System.out.println("successfully applied a move " + move.getId());
            } catch (IllegalArgumentException e) {
                System.err.println("oops! " + e.getMessage());
            }
        }
    }

    @Override
    public String getName() {
        return "move_add";
    }

    @Override
    public String getDescription() {
        return "<batch_id> creates a stock move";
    }
}
