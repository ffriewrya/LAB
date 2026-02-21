package ru.itmo.moona;

import java.time.Instant;
import java.util.HashMap;

public final class StockMove {
    private long id;
    private long batchId;
    private StockMoveType type;
    private double quantity;
    private BatchUnit unit;
    private String reason;
    private String ownerUsername;
    private Instant movedAt;
    private Instant createdAt;

    public long getId() {
        return id;
    }


    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        if (StockManager.isBatchExists(batchId)) {
            this.batchId = batchId;
            this.unit = StockManager.setMoveUnit(batchId);
        } else {
            throw new IllegalArgumentException("batchId doesn't exists");
        }
    }

    public StockMoveType getType() {
        return type;
    }

    public void setType(String type) {
        this.type = StockManager.findType(type);
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity can't be negative");
        } else {
            this.quantity = quantity; }
    }

    public BatchUnit getUnit() {
        return unit;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        if (reason.length() > 128) {
            throw new IllegalArgumentException("reason can't be above 128 symbols");
        } else {
            this.reason = reason;
        }
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public Instant getMovedAt() {
        return movedAt;
    }

    public void setMovedAt(Instant movedAt) {
        this.movedAt = movedAt;
        //подумать как сделать апдейт
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    private StockMove(MoveBuilder moveBuilder) {
        this.id = moveBuilder.id;
        this.batchId = moveBuilder.batchId;
        this.type = moveBuilder.type;
        this.quantity = moveBuilder.quantity;
        this.unit = moveBuilder.unit;
        this.reason = moveBuilder.reason;
        this.ownerUsername = moveBuilder.ownerUsername;
        this.movedAt = moveBuilder.movedAt;
        this.createdAt = moveBuilder.createdAt;
    }

    public static class MoveBuilder {
        private long id;
        private long batchId;
        private StockMoveType type;
        private double quantity;
        private BatchUnit unit;
        private String reason;
        private String ownerUsername;
        private Instant movedAt;
        private Instant createdAt;


        public MoveBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public MoveBuilder setBatchId(long batchId) {
            if (StockManager.isBatchExists(batchId)) {
                this.batchId = batchId;
                return this;
            } else {
                throw new IllegalArgumentException("batchId doesn't exists");
            }
        }

        public MoveBuilder setType(String type) {
            this.type = StockManager.findType(type);
            return this;
        }

        public MoveBuilder setQuantity(double quantity) {
            if (quantity < 0) {
                throw new IllegalArgumentException("quantity can't be negative");
            } else {
                this.quantity = quantity;
                return this;
            }
        }

        public MoveBuilder setUnit() {
            this.unit = StockManager.setMoveUnit(this.batchId);
            return this;
        }

        public MoveBuilder setReason(String reason) {
            if (reason.length() > 128) {
                throw new IllegalArgumentException("reason can't be above 128 symbols");
            } else {
                this.reason = reason;
                return this;
            }
        }

        public MoveBuilder setOwnerUsername(String ownerUsername) {
            this.ownerUsername = ownerUsername;
            return this;
        }

        public MoveBuilder setMovedAt(Instant movedAt) {
            if (movedAt == null) {
                this.movedAt = Instant.now();
                return this;
            } else {
                this.movedAt = movedAt;
                return this;
            }
        }

        public MoveBuilder setCreatedAt() {
            this.createdAt = Instant.now();
            return this;
        }

        public StockMove build() {
            return new StockMove(this);
        }

    }
}
