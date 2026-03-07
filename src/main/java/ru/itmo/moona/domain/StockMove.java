package ru.itmo.moona.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.service.StockUtils;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@JsonAutoDetect
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

    private StockMove() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public StockMoveType getType() {
        return type;
    }

    public void setType(StockMoveType type) {
        this.type = type;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public BatchUnit getUnit() {
        return unit;
    }

    public void setUnit(BatchUnit unit) {
        this.unit = unit;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
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
        return String.format("%-4s %-10s %-15s %-10s %-10s %-15s %-15s %-15s %-25s", id, batchId, type, quantity, unit, reason, ownerUsername, StockUtils.formatterExp.format(movedAt), StockUtils.formatter.format(createdAt));
    }

    @JsonIgnore
    public boolean isValid() {
        return type != null && quantity > 0 && unit != null && (reason == null || reason.length() <= 128) && movedAt != null && createdAt != null && ownerUsername != null;
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
                this.batchId = batchId;
                return this;
        }

        public MoveBuilder setType(String type) {
            this.type = StockUtils.findType(type);
            return this;
        }

        public MoveBuilder setQuantity(double quantity) {
            if (quantity < 0) {
                throw new IllegalArgumentException("quantity can't be negative.");
            } else {
                this.quantity = quantity;
                return this;
            }
        }

        public MoveBuilder setUnit(BatchUnit unit) {
            this.unit = unit;
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
                this.movedAt = StockUtils.parseDate(movedAt);
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
