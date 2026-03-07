package ru.itmo.moona.storage;

import ru.itmo.moona.domain.Reagent;
import ru.itmo.moona.domain.ReagentBatch;
import ru.itmo.moona.domain.StockMove;

import java.util.HashMap;

public class StockSnapshot {
    private HashMap<Long, Reagent> rgs = new HashMap<>();
    private HashMap<Long, ReagentBatch> bchs = new HashMap<>();
    private HashMap<Long, StockMove> mvs = new HashMap<>();

    public StockSnapshot(HashMap<Long, Reagent> r, HashMap<Long,ReagentBatch> b, HashMap<Long, StockMove> m) {
        this.rgs = r;
        this.bchs = b;
        this.mvs = m;
    }

    public StockSnapshot() {}

    public HashMap<Long, Reagent> getRgs() {
        return rgs;
    }

    public void setRgs(HashMap<Long, Reagent> rgs) {
        this.rgs = rgs;
    }

    public HashMap<Long, ReagentBatch> getBchs() {
        return bchs;
    }

    public void setBchs(HashMap<Long, ReagentBatch> bchs) {
        this.bchs = bchs;
    }

    public HashMap<Long, StockMove> getMvs() {
        return mvs;
    }

    public void setMvs(HashMap<Long, StockMove> mvs) {
        this.mvs = mvs;
    }
}
