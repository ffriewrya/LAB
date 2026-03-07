package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Undoable;
import ru.itmo.moona.domain.ReagentBatch;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;
import ru.itmo.moona.service.StockUtils;

import java.util.Scanner;

public class AddBatchCommand implements Command, Undoable {
    private final Scanner scanner;
    private ReagentBatch createdBatch;
    private final StockManager manager;

    public AddBatchCommand(Scanner scanner, StockManager manager) {
        this.scanner = scanner;
        this.manager = manager;
    }

    @Override
    public void execute(InputParser input) {
        if (input.getArg() == null || input.getArg().isBlank()) {
            throw new IllegalArgumentException("expected a reagent ID");
        } else {
            ReagentBatch.BatchBuilder builder = new ReagentBatch.BatchBuilder();
            builder.setId(manager.genBatchId());
            builder.setCreatedAt();
            builder.setUpdatedAt();
            builder.setOwnerUsername("SYSTEM");

            try {
                String idInput = input.getArg();
                long id = Long.parseLong(idInput);
                if (!manager.reagentExists(id)) {
                    throw new IllegalArgumentException("reagentId doesn't exist");
                }
                builder.setReagentId(id);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("invalid id. expected a number");
            }

            while (true) {
                try {
                    System.out.println("enter label");
                    String label = scanner.nextLine();
                    builder.setLabel(label);
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }

            while (true) {
                try {
                    System.out.println("enter current quantity");
                    String quantityInput = scanner.nextLine();
                    try {
                        double quantity = Double.parseDouble(quantityInput);
                        builder.setQuantityCurrent(quantity);
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
                    System.out.println("enter unit");
                    String unit = scanner.nextLine();
                    builder.setUnit(unit);
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }

            while (true) {
                try {
                    System.out.println("enter location");
                    String location = scanner.nextLine();
                    builder.setLocation(location);
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }

            while (true) {
                try {
                    System.out.println("enter status");
                    String status = scanner.nextLine();
                    builder.setStatus(status);
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }

            while (true) {
                try {
                    System.out.println("enter expiration date (optional)");
                    String date = scanner.nextLine();
                    builder.setExpiresAt(date);
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }

            createdBatch = builder.build();
            manager.addBatch(createdBatch);
            System.out.println("successfully added a batch " + createdBatch.getId());
        }
    }

    @Override
    public String getName() {
        return "batch_add";
    }

    @Override
    public String getDescription() {
        return "<reagent_id> creates a batch of existing reagent";
    }

    @Override
    public void undo() {
        if (createdBatch == null) {
            throw new IllegalArgumentException("there is no created reagent. can't undo.");
        }
        manager.removeBatch(createdBatch);
        System.out.println("successfully removed batch " + createdBatch.getId());
    }


    @Override
    public void redo() {
        if (createdBatch == null) {
            throw new IllegalArgumentException("there is no created reagent. can't redo.");
        }
        if (!manager.batchExists(createdBatch.getId())) {
            manager.redoBatch(createdBatch);
            System.out.println("batch " + createdBatch.getId() + " has been re-added.");
        } else {
            throw new IllegalArgumentException("reagent already exists. can't redo.");
        }
    }
}

