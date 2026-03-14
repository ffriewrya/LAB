package ru.itmo.moona.storage;

import ru.itmo.moona.domain.Reagent;
import ru.itmo.moona.domain.ReagentBatch;
import ru.itmo.moona.domain.StockMove;
import java.util.HashMap;

public class StockValidator {

    public void validateRId(HashMap<Long, Reagent> map) {
        HashMap<Long, Reagent> validation = new HashMap<>();
        for (Reagent o : map.values()) {
            if (validation.putIfAbsent(o.getId(), o) != null) {
                throw new IllegalArgumentException("invalid reagents. Id " + o.getId() + " occurs several times");
            }
        }
        map.keySet().forEach(i -> {
            if (i != map.get(i).getId()) {
                throw new IllegalArgumentException("invalid file! reagent has " + i + " and " + map.get(i).getId() + " as its key and id" );
            }
        });
    }
    public void validateBId(HashMap<Long, ReagentBatch> map) {
        HashMap<Long, ReagentBatch> validation = new HashMap<>();
        for (ReagentBatch o : map.values()) {
            if (validation.putIfAbsent(o.getId(), o) != null) {
                throw new IllegalArgumentException("invalid batches. Id " + o.getId() + " occurs several times");
            }
        }
        map.keySet().forEach(i -> {
            if (i != map.get(i).getId()) {
                throw new IllegalArgumentException("invalid file! batch has " + i + " and " + map.get(i).getId() + " as its key and id" );
            }
        });
    }
    public void validateMId(HashMap<Long, StockMove> map) {
        HashMap<Long, StockMove> validation = new HashMap<>();
        for (StockMove o : map.values()) {
            if (validation.putIfAbsent(o.getId(), o) != null) {
                throw new IllegalArgumentException("invalid moves. Id " + o.getId() + " occurs several times");
            }
        }
        map.keySet().forEach(i -> {
            if (i != map.get(i).getId()) {
                throw new IllegalArgumentException("invalid file! move has " + i + " and " + map.get(i).getId() + " as its key and id" );
            }
        });
    }

    public void validate(StockSnapshot s) {
        if (!s.getRgs().isEmpty()) {
            validateRId(s.getRgs());
            s.getRgs().forEach((id, r) -> {
                try {
                    r.isValid();
                } catch (Exception e) {
                    throw new IllegalArgumentException("invalid file! " + id + " reagent's " + e.getMessage());
                }
            });
        }
        if (!s.getBchs().isEmpty()) {
            validateBId(s.getBchs());
            s.getBchs().forEach((id, b) -> {
                try {
                    b.isValid();
                } catch (Exception e) {
                    throw new IllegalArgumentException("invalid file! " + id + " batch's " + e.getMessage());
                }
                if (!s.getRgs().containsKey(b.getReagentId())) {
                    throw new IllegalArgumentException("invalid batch " + b.getId() + ". reagentId doesn't exist.");
                }
            });
        }
        if (!s.getMvs().isEmpty()) {
            validateMId(s.getMvs());
            s.getMvs().forEach((id, m) -> {
                try {
                    m.isValid();
                } catch (Exception e) {
                    throw new IllegalArgumentException("invalid file! " + id + " move's " + e.getMessage());
                }
                if (!s.getBchs().containsKey(m.getBatchId())) {
                    throw new IllegalArgumentException("invalid move " + m.getId() + ". batchId doesn't exist.");
                }
            });
        }
    }
}

