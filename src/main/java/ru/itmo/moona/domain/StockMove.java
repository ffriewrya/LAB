package ru.itmo.moona.domain;

import ru.itmo.moona.service.StockManager;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static ru.itmo.moona.service.StockManager.*;

public final class StockMove {
    private final long id;
    private long batchId;
    private StockMoveType type;
    private double quantity;
    private BatchUnit unit;
    private String reason;
    private String ownerUsername;
    private Instant movedAt;
    private final Instant createdAt;

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
            throw new IllegalArgumentException("batchId doesn't exist.");
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
            throw new IllegalArgumentException("quantity can't be negative.");
        } else {
            this.quantity = quantity;
        }
    }

    public BatchUnit getUnit() {
        return unit;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        if (reason.length() > 128) {
            throw new IllegalArgumentException("reason can't have a length exceeding 128 characters.");
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
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockMove stockMove = (StockMove) o;
        return id == stockMove.id && batchId == stockMove.batchId && Double.compare(quantity, stockMove.quantity) == 0 && type == stockMove.type && unit == stockMove.unit && Objects.equals(reason, stockMove.reason) && Objects.equals(ownerUsername, stockMove.ownerUsername) && Objects.equals(movedAt, stockMove.movedAt) && Objects.equals(createdAt, stockMove.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, batchId, type, quantity, unit, reason, ownerUsername, movedAt, createdAt);
    }

    @Override
    public String toString() {
        return String.format("%-4s %-10s %-15s %-10s %-10s %-15s %-15s %-15s %-25s", id, batchId, type, quantity, unit, reason, ownerUsername, formatterExp.format(movedAt), formatter.format(createdAt));
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
                throw new IllegalArgumentException("batchId doesn't exist.");
            }
        }

        public MoveBuilder setType(String type) {
            this.type = StockManager.findType(type);
            return this;
        }

        public StockMoveType getType() {
            return type;
        }

        public MoveBuilder setQuantity(double quantity) {
            if (quantity < 0) {
                throw new IllegalArgumentException("quantity can't be negative.");
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
                throw new IllegalArgumentException("reason can't have a length exceeding 128 characters.");
            } else {
                this.reason = reason;
                return this;
            }
        }

        public MoveBuilder setOwnerUsername(String ownerUsername) {
            this.ownerUsername = ownerUsername;
            return this;
        }

        public MoveBuilder setMovedAt() {
            this.movedAt = Instant.now();
            return this;
        }

        public MoveBuilder setMovedAt(String movedAt) {
            try {
                this.movedAt = parseDate(movedAt);
                return this;
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("invalid moving date. expected dd-MM-yyyy.");
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
