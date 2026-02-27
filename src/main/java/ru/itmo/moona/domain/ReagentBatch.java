package ru.itmo.moona.domain;

import ru.itmo.moona.service.StockManager;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.itmo.moona.service.StockManager.*;

public final class ReagentBatch {
    private final long id;
    private long reagentId;
    private String label;
    private double quantityCurrent;
    private BatchUnit unit;
    private String location;
    private Instant expiresAt;
    private BatchStatus status;
    private String ownerUsername;
    private final Instant createdAt;
    private Instant updatedAt;

    private ReagentBatch(BatchBuilder batchBuilder) {
        this.id = batchBuilder.id;
        this.reagentId = batchBuilder.reagentId;
        this.label = batchBuilder.label;
        this.quantityCurrent = batchBuilder.quantityCurrent;
        this.unit = batchBuilder.unit;
        this.location = batchBuilder.location;
        this.expiresAt = batchBuilder.expiresAt;
        this.status = batchBuilder.status;
        this.ownerUsername = batchBuilder.ownerUsername;
        this.createdAt = batchBuilder.createdAt;
        this.updatedAt = batchBuilder.updatedAt;
    }

    public long getId() {
        return id;
    }

    public long getReagentId() {
        return reagentId;
    }

    public void setReagentId(long reagentId) {
        if (StockManager.isReagentExists(reagentId)) {
            this.reagentId = reagentId;
        } else {
            throw new IllegalArgumentException("reagentId doesn't exist");
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if (label == null || label.isBlank() || label.length() > 64) {
            throw new IllegalArgumentException("label can't be null or have a length exceeding 64 characters.");
        } else {
            this.label = label;
        }
    }

    public double getQuantityCurrent() {
        return quantityCurrent;
    }

    public void setQuantityCurrent(double quantityCurrent) {
        if (quantityCurrent < 0) {
            throw new IllegalArgumentException("current quantity can't be negative");
        } else {
            this.quantityCurrent = quantityCurrent;
        }
    }

    public BatchUnit getUnit() {
        return unit;
    }

    public void setUnit(BatchUnit unit) {
        this.unit = unit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null || location.isBlank() || location.length() > 64) {
            throw new IllegalArgumentException("location can't be blank or have a length exceeding 64 characters.");
        } else {
            this.location = location;
        }
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = StockManager.findStatus(status);
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ReagentBatch that = (ReagentBatch) o;
        return id == that.id && reagentId == that.reagentId && Double.compare(quantityCurrent, that.quantityCurrent) == 0 && Objects.equals(label, that.label) && unit == that.unit && Objects.equals(location, that.location) && Objects.equals(expiresAt, that.expiresAt) && status == that.status && Objects.equals(ownerUsername, that.ownerUsername) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reagentId, label, quantityCurrent, unit, location, expiresAt, status, ownerUsername, createdAt, updatedAt);
    }


    @Override
    public String toString() {
        return String.format("%-4s %-10s %-15s %-10s %-10s %-15s %-15s %-10s %-15s %-25s %-25s", id, reagentId, label, quantityCurrent, unit, location, formatterExp.format(expiresAt), status, ownerUsername, formatter.format(createdAt), formatter.format(updatedAt));
    }

    public static class BatchBuilder {
        private long id;
        private long reagentId;
        private String label;
        private double quantityCurrent;
        private BatchUnit unit;
        private String location;
        private Instant expiresAt;
        private BatchStatus status;
        private String ownerUsername;
        private Instant createdAt;
        private Instant updatedAt;


        public BatchBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public BatchBuilder setReagentId(long reagentId) {
            if (StockManager.isReagentExists(reagentId)) {
                this.reagentId = reagentId;
                return this;
            } else {
                throw new IllegalArgumentException("reagentId doesn't exist");
            }
        }

        public BatchBuilder setLabel(String label) {
            if (label == null || label.isBlank() || label.length() > 64) {
                throw new IllegalArgumentException("label can't be null or have a length exceeding 64 characters.");
            } else {
                this.label = label;
                return this;
            }
        }

        public BatchBuilder setQuantityCurrent(double quantityCurrent) {
            if (quantityCurrent < 0) {
                throw new IllegalArgumentException("current quantity can't be negative");
            } else {
                this.quantityCurrent = quantityCurrent;
                return this;
            }
        }

        public BatchBuilder setUnit(String unit) {
            this.unit = StockManager.findUnit(unit);
            return this;
        }

        public BatchBuilder setLocation(String location) {
            if (location == null || location.isBlank() || location.length() > 64) {
                throw new IllegalArgumentException("location can't be blank or have a length exceeding 64 characters.");
            } else {
                this.location = location;
                return this;
            }
        }

        public BatchBuilder setExpiresAt(String expiresAt) {
            if (expiresAt == null || expiresAt.isBlank()) {
                this.expiresAt = Instant.now().plus(365, ChronoUnit.DAYS);
                return this;
            }
            try {
                this.expiresAt = parseDate(expiresAt);
                return this;
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("invalid expiration date. expected dd-MM-yyyy");
            }
        }

        public BatchBuilder setStatus(String status) {
            this.status = StockManager.findStatus(status);
            return this;
        }

        public BatchBuilder setOwnerUsername(String ownerUsername) {
            this.ownerUsername = ownerUsername;
            return this;
        }

        public BatchBuilder setCreatedAt() {
            this.createdAt = Instant.now();
            return this;
        }

        public BatchBuilder setUpdatedAt() {
            this.updatedAt = Instant.now();
            return this;
        }

        public ReagentBatch build() {
            return new ReagentBatch(this);
        }


    }
}
