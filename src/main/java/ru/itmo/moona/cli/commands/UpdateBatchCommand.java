package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Undoable;
import ru.itmo.moona.domain.ReagentBatch;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;

import java.util.List;
import java.util.Map;

public class UpdateBatchCommand implements Command, Undoable {

    private long batchId;
    private ReagentBatch.BatchMemento oldMemento;
    private ReagentBatch.BatchMemento newMemento;

    @Override
    public void execute(InputParser input) {
        try {
            if (input.getArg() == null || input.getArg().isBlank()) {
                throw new IllegalArgumentException("expected a batch ID");
            }
            this.batchId = Long.parseLong(input.getArg());
            this.oldMemento = StockManager.getBatches().get(batchId).createMemento();
            List<String> rawInput = input.getAdArgs();
            Map<String, String> parsedInput = InputParser.parseAdArgs(rawInput);
            for (String field : parsedInput.keySet()) {
                switch (field.toLowerCase()) {
                    case "location":
                        StockManager.updLocation(batchId, parsedInput.get(field));
                        System.out.println("updated location to " + parsedInput.get(field));
                        break;
                    case "expiresat":
                        StockManager.updExpiresAt(batchId, parsedInput.get(field));
                        System.out.println("updated expires at to " + parsedInput.get(field));
                        break;
                    case "status":
                        StockManager.updStatus(batchId, parsedInput.get(field));
                        System.out.println("updated status to " + parsedInput.get(field));
                        break;
                    case "label":
                        StockManager.updLabel(batchId, parsedInput.get(field));
                        System.out.println("updated label to " + parsedInput.get(field));
                        break;
                }
            }
            ReagentBatch batch = StockManager.getBatch(batchId);
            this.newMemento = batch.createMemento();
            batch.addMemento(newMemento);
        } catch (NumberFormatException e) {
            System.err.println("invalid id. expected a number");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "batch_update";
    }

    @Override
    public String getDescription() {
        return "<batch_id> field=value ... updates a batch (location, expiresAt, status, label)";
    }

    @Override
    public void undo() {
        if (oldMemento == null) {
            throw new IllegalArgumentException("there is no previous state. can't undo");
        }
        StockManager.getBatch(batchId).restoreFromMemento(oldMemento);
        System.out.println("batch " + this.batchId + " state has been restored.");
    }

    @Override
    public void redo() {
        if (newMemento == null) {
            throw new IllegalArgumentException("there is no new state. can't redo");
        }
        StockManager.getBatch(batchId).restoreFromMemento(newMemento);
        System.out.println("batch " + this.batchId + " state has been redone.");
    }
}
