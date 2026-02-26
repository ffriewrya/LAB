package ru.itmo.moona.commands;

import ru.itmo.moona.ReagentBatch;
import ru.itmo.moona.StockManager;
import ru.itmo.moona.commands.base.Command;
import ru.itmo.moona.commands.base.InputParser;

import java.util.Scanner;

public class AddBatchCommand implements Command {
    private final Scanner scanner;

    public AddBatchCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(InputParser input) {
        if (input.getArg().isBlank()) {
            throw new IllegalArgumentException("expected a reagent ID");
        } else {
            ReagentBatch.BatchBuilder builder = new ReagentBatch.BatchBuilder();
            builder.setId(StockManager.genBatchId());
            builder.setCreatedAt();
            builder.setUpdatedAt();
            builder.setOwnerUsername("SYSTEM");

            while (true) {
                try {
                    System.out.println("enter existing reagent id");
                    String idInput = input.getArg();
                    long id = Long.parseLong(idInput);
                    builder.setReagentId(id);
                    break;
                } catch (NumberFormatException e) {
                    System.err.println("invalid id. expected a number");
                }
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
                    System.out.println("enter expiration date");
                    String date = scanner.nextLine();
                    builder.setExpiresAt(date);
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }

            ReagentBatch batch = builder.build();
            StockManager.addBatch(batch);
            System.out.println("successfully added a batch " + batch.getId());
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
}
