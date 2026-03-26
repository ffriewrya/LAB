package ru.itmo.moona.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.itmo.moona.service.StockUtils;

import java.time.Instant;
import java.util.Objects;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public final class StockMove {
    private long id;
    private long batchId;
    private StockMoveType type;
    private double quantity;
    private BatchUnit unit;
    private String reason;
    private Long ownerId;
    private Instant movedAt;
    private Instant createdAt;

    private StockMove() {
    }

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
        return id == stockMove.id && batchId == stockMove.batchId && Double.compare(quantity, stockMove.quantity) == 0 && type == stockMove.type && unit == stockMove.unit && Objects.equals(reason, stockMove.reason) && Objects.equals(ownerId, stockMove.ownerId) && Objects.equals(movedAt, stockMove.movedAt) && Objects.equals(createdAt, stockMove.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, batchId, type, quantity, unit, reason, ownerId, movedAt, createdAt);
    }

    @Override
    public String toString() {
        return String.format("%-4s %-10s %-15s %-10s %-10s %-15s %-15s %-15s %-25s", id, batchId, type, quantity, unit, reason, ownerId, StockUtils.formatterExp.format(movedAt), StockUtils.formatter.format(createdAt));
    }

    @JsonIgnore
    public void isValid() {
        if (type == null) {
            throw new IllegalArgumentException("type can't be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        if (unit == null) {
            throw new IllegalArgumentException("unit can't be null");
        }
        if (reason != null && reason.length() > 128) {
            throw new IllegalArgumentException("reason can't have length exceeding 128 char");
        }
        if (movedAt == null) {
            throw new IllegalArgumentException("movedAt can't be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("createdAt can't be null");
        }
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId can't be null");
        }
    }

    private StockMove(MoveBuilder moveBuilder) {
        this.id = moveBuilder.id;
        this.batchId = moveBuilder.batchId;
        this.type = moveBuilder.type;
        this.quantity = moveBuilder.quantity;
        this.unit = moveBuilder.unit;
        this.reason = moveBuilder.reason;
        this.ownerId = moveBuilder.ownerId;
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
        private Long ownerId;
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

        public MoveBuilder setType(StockMoveType type) {
            this.type = type;
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

        public MoveBuilder setOwnerId(Long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public MoveBuilder setMovedAt() {
            this.movedAt = Instant.now();
            return this;
        }

        public MoveBuilder setMovedAt(Instant movedAt) {
            this.movedAt = movedAt;
            return this;
        }

        public MoveBuilder setCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public StockMove build() {
            return new StockMove(this);
        }

    }
}
