package ru.itmo.moona.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.service.StockUtils;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonAutoDetect
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

    private ReagentBatch() {
    }

    private final List<BatchMemento> history = new ArrayList<>();

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

    public void setId(long id) {
        this.id = id;
    }

    public long getReagentId() {
        return reagentId;
    }

    public void setReagentId(long reagentId) {
        this.reagentId = reagentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getQuantityCurrent() {
        return quantityCurrent;
    }

    public void setQuantityCurrent(double quantityCurrent) {
        this.quantityCurrent = quantityCurrent;
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
        this.location = location;
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

    public void setStatus(BatchStatus status) {
        this.status = status;
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

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<BatchMemento> getHistory() {
        return history;
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
        return String.format("%-4s %-10s %-15s %-10s %-10s %-15s %-15s %-10s %-15s %-25s %-25s", id, reagentId, label, quantityCurrent, unit, location, StockUtils.formatterExp.format(expiresAt), status, ownerUsername, StockUtils.formatter.format(createdAt), StockUtils.formatter.format(updatedAt));
    }

    @JsonIgnore
    public boolean isValid() {
        return label != null && !label.isBlank() && label.length() <= 64 && quantityCurrent >= 0 &&  location != null && !location.isBlank() && location.length() <= 64 && expiresAt != null && unit != null && updatedAt != null && createdAt != null && ownerUsername != null;
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
                this.reagentId = reagentId;
                return this;
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
            this.unit = StockUtils.findUnit(unit);
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
                this.expiresAt = StockUtils.parseDate(expiresAt);
                return this;
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("invalid expiration date. expected dd-MM-yyyy");
            }
        }

        public BatchBuilder setStatus(String status) {
            this.status = StockUtils.findStatus(status);
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
            ReagentBatch batch = new ReagentBatch(this);
            batch.addMemento(batch.createMemento());
            return batch;
        }


    }

    public void addMemento(BatchMemento m) {
        this.history.add(m);
    }

    public BatchMemento createMemento() {
        return new BatchMemento(this.reagentId, this.label, this.quantityCurrent, this.unit, this.location, this.expiresAt, this.status, this.ownerUsername, this.createdAt, this.updatedAt);
    }

    public void restoreStatusFromMemento(BatchMemento m) {
        this.status = m.status;
    }

    public void restoreFromMemento(BatchMemento m) {
        this.reagentId = m.reagentId;
        this.label = m.label;
        this.quantityCurrent = m.quantityCurrent;
        this.unit = m.unit;
        this.location = m.location;
        this.expiresAt = m.expiresAt;
        this.status = m.status;
        this.ownerUsername = m.ownerUsername;
        this.createdAt = m.createdAt;
        this.updatedAt = m.updatedAt;
    }

    @JsonAutoDetect
    public static class BatchMemento {
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

        private BatchMemento() {
        }

        private BatchMemento(long reagentId, String label, double quantityCurrent, BatchUnit unit, String location, Instant expiresAt, BatchStatus status, String ownerUsername, Instant createdAt, Instant updatedAt) {
            this.reagentId = reagentId;
            this.label = label;
            this.quantityCurrent = quantityCurrent;
            this.unit = unit;
            this.location = location;
            this.expiresAt = expiresAt;
            this.status = status;
            this.ownerUsername = ownerUsername;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public long getReagentId() {
            return reagentId;
        }

        public void setReagentId(long reagentId) {
            this.reagentId = reagentId;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public double getQuantityCurrent() {
            return quantityCurrent;
        }

        public void setQuantityCurrent(double quantityCurrent) {
            this.quantityCurrent = quantityCurrent;
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
            this.location = location;
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

        public void setStatus(BatchStatus status) {
            this.status = status;
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

        public void setCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
        }

        public Instant getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
        }

        @Override
        public String toString() {
            return String.format("%-10s %-15s %-10s %-10s %-15s %-15s %-10s %-15s %-25s %-25s", reagentId, label, quantityCurrent, unit, location, StockUtils.formatterExp.format(expiresAt), status, ownerUsername, StockUtils.formatter.format(createdAt), StockUtils.formatter.format(updatedAt));
        }

    }
}
