package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Undoable;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.domain.StockMove;
import ru.itmo.moona.domain.StockMoveType;
import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;
import ru.itmo.moona.service.StockUtils;

import java.util.Scanner;

public class AddMoveCommand implements Command, Undoable {
    private final Scanner scanner;
    private StockMove createdMove;
    private final StockManager manager;

    public AddMoveCommand(Scanner scanner, StockManager manager) {
        this.scanner = scanner;
        this.manager = manager;
    }

    @Override
    public void execute(InputParser input) {
        if (input.getArg() == null || input.getArg().isBlank()) {
            throw new IllegalArgumentException("expected a batch ID");
        } else {
            StockMove.MoveBuilder builder = new StockMove.MoveBuilder();
            builder.setId(manager.genMoveId());
            builder.setCreatedAt();
            builder.setOwnerUsername("SYSTEM");
            String idInput = input.getArg();

            try {
                long id = Long.parseLong(idInput);
                if (!manager.batchExists(id)) {
                    throw new IllegalArgumentException("this batch doesn't exist.");
                }
                if (manager.isBatchArchived(id)) {
                    throw new IllegalArgumentException("you can't move an archived batch");
                } else {
                    builder.setBatchId(id);
                    builder.setUnit(manager.setMoveUnit(id));
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("invalid id. expected a number");
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
                    System.out.println("enter reason (optional)");
                    String reason = scanner.nextLine();
                    builder.setReason(reason);
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }

            while (true) {
                try {
                    System.out.println("enter moving date (optional)");
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
            try {
                createdMove = builder.build();
                if (createdMove.getType() == StockMoveType.IN) {
                    manager.moveIn(createdMove);
                } else {
                    manager.moveOutDiscard(createdMove);
                }
                manager.addMove(createdMove);
                System.out.println("successfully applied move " + createdMove.getId());
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

    @Override
    public void undo() {
        if (createdMove == null) {
            throw new IllegalArgumentException("there is no created move. can't undo");
        }
        manager.removeMove(createdMove);
        if (createdMove.getType() == StockMoveType.IN) {
            manager.moveOutDiscard(createdMove);
        } else {
            manager.moveIn(createdMove);
        }
        System.out.println("move " + createdMove.getId() + " has been removed.");

    }

    @Override
    public void redo() {
        if (createdMove == null) {
            throw new IllegalArgumentException("there is no created move. can't redo.");
        }
        if (!manager.moveExists(createdMove.getId())) {
            manager.redoMove(createdMove);
            if (createdMove.getType() == StockMoveType.IN) {
                manager.moveIn(createdMove);
            } else {
                manager.moveOutDiscard(createdMove);
            }
            System.out.println("move " + createdMove.getId() + " has been re-added.");
        } else {
            throw new IllegalArgumentException("move already exists. can't redo.");
        }

    }
}
