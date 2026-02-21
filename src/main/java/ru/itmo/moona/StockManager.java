package ru.itmo.moona;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class StockManager {
    private static final HashMap<Long, Reagent> reagents = new HashMap<>();
    private static final HashMap<Long, ReagentBatch> batches = new HashMap<>();
    private Long ReagentId = 0L;
    private Long BatchId = 0L;

    public Long genRId() {
        Long generatedId = 1L + ReagentId;
        return generatedId;
    }

    public Long genBId() {
        Long generatedId = 1L + BatchId;
        return generatedId;
    }

    public void addBatch(ReagentBatch b) {
        batches.put(b.getId(), b);
        BatchId++;
    }

    public void addReagent(Reagent r) {
        reagents.put(r.getId(), r);
        ReagentId++;

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
        throw new IllegalArgumentException("unit should be G or ML");

    }

    public static BatchStatus findStatus(String status) {
        for (BatchStatus bs : BatchStatus.values()) {
            if (bs.name().equalsIgnoreCase(status)) {
                return bs;
            }
        }
        throw new IllegalArgumentException("status should be ACTIVE or ARCHIVED");

    }

    public static StockMoveType findType(String type) {
        for (StockMoveType mt : StockMoveType.values()) {
            if (mt.name().equalsIgnoreCase(type)) {
                return mt;
            }
        }
        throw new IllegalArgumentException("type should be IN, OUT or DISCARD");
    }

    public static BatchUnit setMoveUnit (long batchId) {
        BatchUnit unit = batches.get(batchId).getUnit();
        return unit;
    }

    public HashMap<Long, Reagent> getReagents() {
        return reagents;
    }

    public HashMap<Long, ReagentBatch> getBatches() {
        return batches;
    }

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d.MM.yyyy HH:mm:ss")
            .withZone(ZoneId.systemDefault());
}

