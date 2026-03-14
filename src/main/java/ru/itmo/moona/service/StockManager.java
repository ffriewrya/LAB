package ru.itmo.moona.service;

import ru.itmo.moona.domain.*;
import ru.itmo.moona.storage.StockSnapshot;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;

public class StockManager {
    private final HashMap<Long, Reagent> reagents = new HashMap<>();
    private final HashMap<Long, ReagentBatch> batches = new HashMap<>();
    private final HashMap<Long, StockMove> moves = new HashMap<>();
    private Long reagentId = 0L;
    private Long batchId = 0L;
    private Long moveId = 0L;

    public void setReagentId(Long reagentId) {
        this.reagentId = reagentId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public void setMoveId(Long moveId) {
        this.moveId = moveId;
    }

    public Long genReagentId() {
        Long generatedId = 1L + reagentId;
        return generatedId;
    }

    public Long genBatchId() {
        Long generatedId = 1L + batchId;
        return generatedId;
    }

    public Long genMoveId() {
        Long generatedId = 1L + moveId;
        return generatedId;
    }

    public void addBatch(ReagentBatch b) {
        batches.put(b.getId(), b);
        batchId++;
    }

    public void addReagent(Reagent r) {
        reagents.put(r.getId(), r);
        reagentId++;
    }

    public void addMove(StockMove m) {
        moves.put(m.getId(), m);
        update(batches.get(m.getBatchId()));
        moveId++;
    }

    public BatchUnit setMoveUnit(long batchId) {
        BatchUnit unit = batches.get(batchId).getUnit();
        return unit;
    }

    public boolean isUnitValid(StockMove m) {
        if (m.getUnit() == setMoveUnit(m.getBatchId())) {
            return true;
        }
        return false;
    }

    public HashMap<Long, Reagent> getReagents() {
        return reagents;
    }

    public HashMap<Long, ReagentBatch> getBatches() {
        return batches;
    }

    public HashMap<Long, StockMove> getMoves() {
        return moves;
    }

    public void update(Reagent r) {
        r.setUpdatedAt(Instant.now());
    }

    public void update(ReagentBatch b) {
        b.setUpdatedAt(Instant.now());
    }

    public void update(StockMove m) {
        m.setMovedAt(Instant.now());
    }

    private void printReagTemplate() {
        System.out.printf("%-4s %-20s %-10s %-15s %-15s %-15s %-25s %-25s", "ID", "Name", "Formula", "CAS", "Hazard Class", "Owner", "Created at", "Updated at");
        System.out.println();
    }

    private void printBatchTemplate() {
        System.out.printf("%-4s %-10s %-15s %-10s %-10s %-15s %-15s %-10s %-15s %-25s %-25s", "ID", "Reagent", "Label", "Quantity", "Unit", "Location", "Expires at", "Status", "Owner", "Created at", "Updated at");
        System.out.println();
    }

    private void printMoveTemplate() {
        System.out.printf("%-4s %-10s %-15s %-10s %-10s %-15s %-15s %-15s %-25s", "ID", "Batch", "Type", "Quantity", "Unit", "Reason", "Owner", "Moved at", "Created at");
        System.out.println();
    }

    public void printReagents() {
        printReagTemplate();
        reagents.values().forEach(System.out::println);
    }

    public void printBatches() {
        printBatchTemplate();
        batches.values().forEach(System.out::println);
    }

    public void printMoves() {
        printMoveTemplate();
        moves.values().forEach(System.out::println);
    }


    public List<Reagent> findReagent(String name) {
        List<Reagent> result = new ArrayList<>();
        for (Reagent reagent : reagents.values()) {
            if (reagent.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(reagent);
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("haven't found any reagents.");
        } else {
            return result;
        }
    }

    public List<ReagentBatch> findBatch(long id) {
        List<ReagentBatch> result = new ArrayList<>();
        for (ReagentBatch batch : batches.values()) {
            if (batch.getReagentId() == id) {
                result.add(batch);
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("haven't found any batches.");
        } else {
            return result;
        }
    }

    public List<ReagentBatch> findActiveBatch(long id) {
        List<ReagentBatch> result = new ArrayList<>();
        for (ReagentBatch batch : batches.values()) {
            if (batch.getReagentId() == id && batch.getStatus() == BatchStatus.ACTIVE) {
                result.add(batch);
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("haven't found any active batches.");
        }
        return result;
    }
    public List<ReagentBatch> findActiveBatches() {
        List<ReagentBatch> result = new ArrayList<>();
        for (ReagentBatch batch : batches.values()) {
            if (batch.getStatus() == BatchStatus.ACTIVE) {
                result.add(batch);
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("haven't found any active batches.");
        }
        return result;
    }

    public void showBatch(long id) {
        if (batchExists(id)) {
            ReagentBatch batch = batches.get(id);
            printBatchTemplate();
            System.out.println(batch);
        } else {
            throw new IllegalArgumentException("batch ID doesn't exist");
        }
    }

    public List<StockMove> showMoves(long id) {
        List<StockMove> result = new ArrayList<>();
        for (StockMove move : moves.values()) {
            if (move.getBatchId() == id) {
                result.add(move);
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("haven't found any moves.");
        } else {
            return result;
        }
    }

    public void showAmountOfMoves(long id, int amount) {
        List<StockMove> result = new ArrayList<>();
        for (StockMove move : moves.values()) {
            if (move.getBatchId() == id) {
                result.add(move);
                if (result.size() > amount) {
                    result.remove(0);
                }
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("haven't found any moves.");
        } else {
            printMoveTemplate();
            result.forEach(System.out::println);
        }
    }

    public void archiveBatch(long id) {
        if (batchExists(id)) {
            ReagentBatch b = batches.get(id);
            if (b.getStatus() == BatchStatus.ARCHIVED) {
                throw new IllegalArgumentException("this batch is already archived.");
            } else {
                b.setStatus(BatchStatus.ARCHIVED);
                System.out.println("batch " + id + " now is archived.");
            }
        } else {
            throw new IllegalArgumentException("haven't found any batch");
        }
    }

    public void stockReport() {
        System.out.println("Reagents:");
        printReagents();
        System.out.println("Batches:");
        printBatches();
        System.out.println("Moves:");
        printMoves();
    }

    public void stockReport(String date) {
        try {
            Instant targetDate = StockUtils.parseDate(date);
            List<ReagentBatch> result = new ArrayList<>();
            for (ReagentBatch b : batches.values()) {
                if (b.getExpiresAt().isBefore(targetDate)) {
                    result.add(b);
                }
            }

            if (result.isEmpty()) {
                throw new IllegalArgumentException("haven't found any batches.");
            } else {
                printBatchTemplate();
                result.forEach(System.out::println);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("invalid date format. expected dd-MM-yyyy.");
        }
    }

    public void moveIn(StockMove move) {
        ReagentBatch batch = batches.get(move.getBatchId());
        double quantity = move.getQuantity();
        double currentQuantity = batch.getQuantityCurrent();
        batch.setQuantityCurrent(currentQuantity + quantity);
        batch.addMemento(batch.createMemento());
    }

    public void moveOutDiscard(StockMove move) {
        ReagentBatch batch = batches.get(move.getBatchId());
        double quantity = move.getQuantity();
        double currentQuantity = batch.getQuantityCurrent();
        if (quantity > currentQuantity) {
            throw new IllegalArgumentException("insufficient quantity. current stock is less than the requested amount to move");
        } else {
            batch.setQuantityCurrent(currentQuantity - quantity);
            batch.addMemento(batch.createMemento());
        }
    }

    public boolean isBatchArchived(long id) {
        if (batches.get(id).getStatus() == BatchStatus.ARCHIVED) {
            return true;
        } else {
            return false;
        }
    }

    public void updLocation(long id, String location) {
        ReagentBatch batch = batches.get(id);
        batch.setLocation(location);
        update(batch);
    }

    public void updExpiresAt(long id, Instant date) {
        ReagentBatch batch = batches.get(id);
        batch.setExpiresAt(date);
        update(batch);
    }

    public void updStatus(long id, BatchStatus status) {
        ReagentBatch batch = batches.get(id);
        batch.setStatus(status);
        update(batch);
    }

    public void updLabel(long id, String label) {
        ReagentBatch batch = batches.get(id);
        batch.setLabel(label);
        update(batch);
    }

    public void removeReagent(Reagent r) {
        reagents.remove(r.getId());
    }

    public void redoReagent(Reagent r) {
        reagents.put(r.getId(), r);
    }

    public void removeBatch(ReagentBatch b) {
        batches.remove(b.getId());
    }

    public void redoBatch(ReagentBatch b) {
        batches.put(b.getId(), b);
    }

    public void removeMove(StockMove m) {
        moves.remove(m.getId());
    }

    public void redoMove(StockMove m) {
        moves.put(m.getId(), m);
    }

    public ReagentBatch getBatch(long id) {
        if (batchExists(id)) {
            return batches.get(id);
        } else {
            throw new IllegalArgumentException("invalid id. haven't found any batches");
        }
    }

    public void printHistory(ReagentBatch batch) {
        List<ReagentBatch.BatchMemento> history = batch.getHistory();
        System.out.printf("%-10s %-15s %-10s %-10s %-15s %-15s %-10s %-15s %-25s %-25s", "Reagent", "Label", "Quantity", "Unit", "Location", "Expires at", "Status", "Owner", "Created at", "Updated at");
        for (int i = 0; i < history.size(); i++) {
            System.out.println();
            System.out.println(history.get(i).toString());
        }
    }


    public boolean reagentExists(long idToCheck) {
        return reagents.containsKey(idToCheck);
    }

    public boolean batchExists(long idToCheck) {
        return batches.containsKey(idToCheck);
    }

    public boolean moveExists(long idToCheck) {
        return moves.containsKey(idToCheck);
    }

    public void loadStock(StockSnapshot s) {
        reagents.clear();
        reagents.putAll(s.getRgs());
        batches.clear();
        batches.putAll(s.getBchs());
        moves.clear();
        moves.putAll(s.getMvs());


        reagentId = reagents.isEmpty() ? 0L : Collections.max(reagents.keySet());
        batchId = batches.isEmpty() ? 0L : Collections.max(batches.keySet());
        moveId = moves.isEmpty()? 0L : Collections.max(moves.keySet());
    }
}

