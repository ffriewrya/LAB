package ru.itmo.moona;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockManager {
    private static final HashMap<Long, Reagent> reagents = new HashMap<>();
    private static final HashMap<Long, ReagentBatch> batches = new HashMap<>();
    private static final HashMap<Long, StockMove> moves = new HashMap<>();
    private static Long reagentId = 0L;
    private static Long batchId = 0L;
    private static Long moveId = 0L;

    public static Long genReagentId() {
        Long generatedId = 1L + reagentId;
        return generatedId;
    }

    public static Long genBatchId() {
        Long generatedId = 1L + batchId;
        return generatedId;
    }

    public static Long genMoveId() {
        Long generatedId = 1L + moveId;
        return generatedId;
    }

    public static void addBatch(ReagentBatch b) {
        batches.put(b.getId(), b);
        batchId++;
    }

    public static void addReagent(Reagent r) {
        reagents.put(r.getId(), r);
        reagentId++;
    }

    public static void addMove(StockMove m) {
        moves.put(m.getId(), m);
        update(batches.get(m.getBatchId()));
        moveId++;
    }

    public static boolean isReagentExists(long idToCheck) {
        return reagents.containsKey(idToCheck);
    }

    public static boolean isBatchExists(long idToCheck) {
        return batches.containsKey(idToCheck);
    }

    public static BatchUnit findUnit(String unit) {
        for (BatchUnit bu : BatchUnit.values()) {
            if (bu.name().equalsIgnoreCase(unit)) {
                return bu;
            }
        }
        throw new IllegalArgumentException("invalid unit. expected G or ML.");

    }

    public static BatchStatus findStatus(String status) {
        for (BatchStatus bs : BatchStatus.values()) {
            if (bs.name().equalsIgnoreCase(status)) {
                return bs;
            }
        }
        throw new IllegalArgumentException("invalid status. expected ACTIVE or ARCHIVED.");

    }

    public static StockMoveType findType(String type) {
        for (StockMoveType mt : StockMoveType.values()) {
            if (mt.name().equalsIgnoreCase(type)) {
                return mt;
            }
        }
        throw new IllegalArgumentException("invalid type. expected IN, OUT or DISCARD.");
    }

    public static BatchUnit setMoveUnit(long batchId) {
        BatchUnit unit = batches.get(batchId).getUnit();
        return unit;
    }

    public static HashMap<Long, Reagent> getReagents() {
        return reagents;
    }

    public HashMap<Long, ReagentBatch> getBatches() {
        return batches;
    }


//    public static String getMethodName() {
//        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
//        String field = name.replace("set", "");
//        return field;
//    }

    public static void update(Reagent r) {
        r.setUpdatedAt(Instant.now());
    }

    public static void update(ReagentBatch b) {
        b.setUpdatedAt(Instant.now());
    }

    public static void update(StockMove m) {
        m.setMovedAt(Instant.now());
    }

    private static void printReagTemplate() {
        System.out.printf("%-4s %-20s %-10s %-15s %-15s %-15s %-25s %-25s", "ID", "Name", "Formula", "CAS", "Hazard Class", "Owner", "Created at", "Updated at");
        System.out.println();
    }

    private static void printBatchTemplate() {
        System.out.printf("%-4s %-10s %-15s %-10s %-10s %-15s %-15s %-10s %-15s %-25s %-25s", "ID", "Reagent", "Label", "Quantity", "Unit", "Location", "Expires at", "Status", "Owner", "Created at", "Updated at");
        System.out.println();
    }

    private static void printMoveTemplate() {
        System.out.printf("%-4s %-10s %-15s %-10s %-10s %-15s %-15s %-15s %-25s", "ID", "Batch", "Type", "Quantity", "Unit", "Reason", "Owner", "Moved at", "Created at");
        System.out.println();
    }

    public static void printReagents() {
        printReagTemplate();
        reagents.values().forEach(System.out::println);
    }

    public static void printBatches() {
        printBatchTemplate();
        batches.values().forEach(System.out::println);
    }

    public static void printMoves() {
        printMoveTemplate();
        moves.values().forEach(System.out::println);
    }

    public static Instant parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        Instant finalDate = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return finalDate;
    }

    public static void findReagent(String name) {
        List<Reagent> result = new ArrayList<>();
        for (Reagent reagent : reagents.values()) {
            if (reagent.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(reagent);
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("haven't found any reagents.");
        } else {
            printReagTemplate();
            result.forEach(System.out::println);
        }
    }

    public static void findBatch(long id) {
        List<ReagentBatch> result = new ArrayList<>();
        for (ReagentBatch batch : batches.values()) {
            if (batch.getReagentId() == id) {
                result.add(batch);
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("haven't found any batches.");
        } else {
            printBatchTemplate();
            result.forEach(System.out::println);
        }
    }

    public static void findActiveBatch(long id) {
        List<ReagentBatch> result = new ArrayList<>();
        for (ReagentBatch batch : batches.values()) {
            if (batch.getReagentId() == id && batch.getStatus() == BatchStatus.ACTIVE) {
                result.add(batch);
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("haven't found any active batches.");
        } else {
            printBatchTemplate();
            result.forEach(System.out::println);
        }
    }

    public static void showBatch(long id) {
        if (isBatchExists(id)) {
            ReagentBatch batch = batches.get(id);
            printBatchTemplate();
            System.out.println(batch);
        } else {
            throw new IllegalArgumentException("batch ID doesn't exist");
        }
    }

    public static void showMoves(long id) {
        List<StockMove> result = new ArrayList<>();
        for (StockMove move : moves.values()) {
            if (move.getBatchId() == id) {
                result.add(move);
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("haven't found any moves.");
        } else {
            printMoveTemplate();
            result.forEach(System.out::println);
        }
    }

    public static void showAmountOfMoves(long id, int amount) {
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

    public static void archiveBatch(long id) {
        if (isBatchExists(id)) {
            ReagentBatch b = batches.get(id);
            if (b.getStatus() == BatchStatus.ARCHIVED) {
                throw new IllegalArgumentException("this batch is already archived.");
            } else {
                b.setStatus("ARCHIVED");
                System.out.println("batch " + id + " now is archived.");
            }
        } else {
            throw new IllegalArgumentException("haven't found any batch");
        }
    }

    public static void stockReport() {
        System.out.println("Reagents:");
        printReagents();
        System.out.println("Batches:");
        printBatches();
        System.out.println("Moves:");
        printMoves();
    }

    public static void stockReport(String date) {
        try {
            Instant targetDate = parseDate(date);
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

    public static void moveIn(StockMove move) {
        ReagentBatch batch = batches.get(move.getBatchId());
        double quantity = move.getQuantity();
        double currentQuantity = batch.getQuantityCurrent();
        batch.setQuantityCurrent(currentQuantity + quantity);
    }

    public static void moveOutDiscard (StockMove move) {
        ReagentBatch batch = batches.get(move.getBatchId());
        double quantity = move.getQuantity();
        double currentQuantity = batch.getQuantityCurrent();
        if (quantity > currentQuantity) {
            throw new IllegalArgumentException("insufficient quantity. current stock is less than the requested amount to move");
        } else {
            batch.setQuantityCurrent(currentQuantity - quantity);
        }
    }

    public static boolean isBatchArcived (long id) {
        if (batches.get(id).getStatus() == BatchStatus.ARCHIVED) {
            return true;
        } else {
            return false;
        }
    }

    public static DateTimeFormatter formatterExp = DateTimeFormatter.ofPattern("d.MM.yyyy")
            .withZone(ZoneId.systemDefault());


    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d.MM.yyyy HH:mm:ss")
            .withZone(ZoneId.systemDefault());
}

