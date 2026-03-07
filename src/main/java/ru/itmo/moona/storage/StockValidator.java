package ru.itmo.moona.storage;

import ru.itmo.moona.domain.Reagent;
import ru.itmo.moona.domain.ReagentBatch;
import ru.itmo.moona.domain.StockMove;
import ru.itmo.moona.service.StockManager;

import java.util.HashMap;

public class StockValidator {

    public void validateRId(HashMap<Long, Reagent> map) {
        HashMap<Long, Reagent> validation = new HashMap<>();
        for (Reagent o : map.values()) {
            if (validation.putIfAbsent(o.getId(), o) != null) {
                throw new IllegalArgumentException("invalid reagents. Id " + o.getId() + " occurs several times");
            }
        }
    }
    public void validateBId(HashMap<Long, ReagentBatch> map) {
        HashMap<Long, ReagentBatch> validation = new HashMap<>();
        for (ReagentBatch o : map.values()) {
            if (validation.putIfAbsent(o.getId(), o) != null) {
                throw new IllegalArgumentException("invalid batches. Id " + o.getId() + " occurs several times");
            }
        }
    }
    public void validateMId(HashMap<Long, StockMove> map) {
        HashMap<Long, StockMove> validation = new HashMap<>();
        for (StockMove o : map.values()) {
            if (validation.putIfAbsent(o.getId(), o) != null) {
                throw new IllegalArgumentException("invalid moves. Id " + o.getId() + " occurs several times");
            }
        }
    }

    public void validate(StockSnapshot s) {
        if (!s.getRgs().isEmpty()) {
            validateRId(s.getRgs());
            s.getRgs().forEach((id, r) -> {
                if (!r.isValid()) {
                    throw new IllegalArgumentException("invalid reagents");
                }
            });
        }
        if (!s.getBchs().isEmpty()) {
            validateBId(s.getBchs());
            s.getBchs().forEach((id, b) -> {
                if (!b.isValid() || !s.getRgs().containsKey(b.getReagentId())) {
                    throw new IllegalArgumentException("invalid batches");
                }
            });
        }
        if (!s.getMvs().isEmpty()) {
            validateMId(s.getMvs());
            s.getMvs().forEach((id, m) -> {
                if (!m.isValid() || !s.getBchs().containsKey(m.getBatchId())) {
                    throw new IllegalArgumentException("invalid reagents");
                }
            });
        }
    }
}

