package ru.itmo.moona;

import java.time.Instant;
import java.util.Objects;

import static ru.itmo.moona.StockManager.*;

public final class ReagentBatch {
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
            update(this);
            System.out.println(getMethodName() + " was changed to " + reagentId);
        } else {
            throw new IllegalArgumentException("reagentId doesn't exists");
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if (label == null || label.isBlank() || label.length() > 64) {
            throw new IllegalArgumentException("label can't be blank or above 64 symbols");
        } else {
            this.label = label;
            update(this);
            System.out.println(getMethodName() + " was changed to " + label);
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
            update(this);
            System.out.println(getMethodName() + " was changed to " + quantityCurrent);
        }
    }

    public BatchUnit getUnit() {
        return unit;
    }

    public void setUnit(BatchUnit unit) {
        this.unit = unit;
        update(this);
        System.out.println(getMethodName() + " was changed to " + unit);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null || location.isBlank() || location.length() > 64) {
            throw new IllegalArgumentException("location can't be blank or above 64 symbols");
        } else {
            this.location = location;
            update(this);
            System.out.println(getMethodName() + " was changed to " + location);
        }
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
        update(this);
        System.out.println(getMethodName() + " was changed to " + expiresAt);
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
        update(this);
        System.out.println(getMethodName() + " was changed to " + status);
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
        update(this);
        System.out.println(getMethodName() + " was changed to " + ownerUsername);
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
        return "ReagentBatch{" +
                "id=" + id +
                ", reagentId=" + reagentId +
                ", label='" + label + '\'' +
                ", quantityCurrent=" + quantityCurrent +
                ", unit=" + unit +
                ", location='" + location + '\'' +
                ", expiresAt=" + expiresAt +
                ", status=" + status +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", createdAt=" + formatter.format(createdAt) +
                ", updatedAt=" + formatter.format(updatedAt) +
                '}';
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
                throw new IllegalArgumentException("reagentId doesn't exists");
            }
        }

        public BatchBuilder setLabel(String label) {
            if (label == null || label.isBlank() || label.length() > 64) {
                throw new IllegalArgumentException("label can't be blank or above 64 symbols");
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
                throw new IllegalArgumentException("location can't be blank or above 64 symbols");
            } else {
                this.location = location;
                return this;
            }
        }

        public BatchBuilder setExpiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
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
