package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Undoable;
import ru.itmo.moona.domain.ReagentBatch;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;

public class BatchArchiveCommand implements Command, Undoable {
    private final StockManager manager;
    private long batchId;
    private ReagentBatch.BatchMemento oldMemento;
    private ReagentBatch.BatchMemento newMemento;

    public BatchArchiveCommand(StockManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(InputParser input) {
        try {
            if (input.getArg() == null || input.getArg().isBlank()) {
                throw new IllegalArgumentException("expected a batch ID");
            } else {
                this.batchId = Long.parseLong(input.getArg());
                ReagentBatch batch = manager.getBatch(batchId);
                this.oldMemento = batch.createMemento();
                manager.archiveBatch(batchId);
                this.newMemento = batch.createMemento();
                batch.addMemento(newMemento);
            }
        } catch (NumberFormatException e) {
            System.err.println("invalid id. expected a number");
        }
    }

    @Override
    public String getName() {
        return "batch_archive";
    }

    @Override
    public String getDescription() {
        return "<batch_id> sets batches status to ARCHIVED.";
    }

    @Override
    public void undo() {
        if (oldMemento == null) {
            throw new IllegalArgumentException("there is no previous state. can't undo");
        }
        manager.getBatch(batchId).restoreStatusFromMemento(oldMemento);
        System.out.println("batch " + this.batchId + " status has been restored");

    }

    @Override
    public void redo() {
        if (newMemento == null) {
            throw new IllegalArgumentException("there is no new state. can't redo");
        }
        manager.getBatch(batchId).restoreStatusFromMemento(newMemento);
        System.out.println("batch " + this.batchId + " status has been redone.");

    }
}
