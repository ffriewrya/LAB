package ru.itmo.moona.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.itmo.moona.service.StockUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ReagentBatch implements Batchable {
    private long id;
    private long reagentId;
    private String label;
    private double quantityCurrent;
    private BatchUnit unit;
    private String location;
    private Instant expiresAt;
    private BatchStatus status;
    private Long ownerId;
    private Instant createdAt;
    private Instant updatedAt;

    private ReagentBatch() {
    }

    private List<BatchMemento> history = new ArrayList<>();

    private ReagentBatch(BatchBuilder batchBuilder) {
        this.id = batchBuilder.id;
        this.reagentId = batchBuilder.reagentId;
        this.label = batchBuilder.label;
        this.quantityCurrent = batchBuilder.quantityCurrent;
        this.unit = batchBuilder.unit;
        this.location = batchBuilder.location;
        this.expiresAt = batchBuilder.expiresAt;
        this.status = batchBuilder.status;
        this.ownerId = batchBuilder.ownerId;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
        return id == that.id && reagentId == that.reagentId && Double.compare(quantityCurrent, that.quantityCurrent) == 0 && Objects.equals(label, that.label) && unit == that.unit && Objects.equals(location, that.location) && Objects.equals(expiresAt, that.expiresAt) && status == that.status && Objects.equals(ownerId, that.ownerId) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reagentId, label, quantityCurrent, unit, location, expiresAt, status, ownerId, createdAt, updatedAt);
    }


    @Override
    public String toString() {
        return label + " id: " + id;
    }

    @JsonIgnore
    public void isValid() {
        if (label == null || label.isBlank() || label.length() > 64) {
            throw new IllegalArgumentException("label can't be blank or have length exceeding 64 char");
        }
        if (quantityCurrent < 0) {
            throw new IllegalArgumentException("quantityCurrent can't be negative");
        }
        if (location == null || location.isBlank() || location.length() > 64) {
            throw new IllegalArgumentException("location can't be blank or have length exceeding 64 char");
        }
        if (expiresAt == null) {
            throw new IllegalArgumentException("expiresAt can't be null");
        }
        if (unit == null) {
            throw new IllegalArgumentException("unit can't be null");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("updatedAt can't be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("createdAt can't be null");
        }
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId can't be null");
        }
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
        private Long ownerId;
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

        public BatchBuilder setUnit(BatchUnit unit) {
            this.unit = unit;
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

        public BatchBuilder setExpiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public BatchBuilder setStatus(BatchStatus status) {
            this.status = status;
            return this;
        }

        public BatchBuilder setOwnerId(Long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public BatchBuilder setCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BatchBuilder setUpdatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ReagentBatch build() {
            ReagentBatch batch = new ReagentBatch(this);
            batch.addMemento(batch.createMemento());
            return batch;
        }


    }

    public void setHistory(List <BatchMemento> m) {
        this.history = m;
    }

    public void addMemento(BatchMemento m) {
        this.history.add(m);
    }

    public BatchMemento createMemento() {
        return new BatchMemento(this.id, this.reagentId, this.label, this.quantityCurrent, this.unit, this.location, this.expiresAt, this.status, this.ownerId, this.createdAt, this.updatedAt);
    }

    public void restoreStatusFromMemento(BatchMemento m) {
        this.status = m.status;
    }

    public void restoreFromMemento(BatchMemento m) {
        this.id = m.id;
        this.reagentId = m.reagentId;
        this.label = m.label;
        this.quantityCurrent = m.quantityCurrent;
        this.unit = m.unit;
        this.location = m.location;
        this.expiresAt = m.expiresAt;
        this.status = m.status;
        this.ownerId = m.ownerId;
        this.createdAt = m.createdAt;
        this.updatedAt = m.updatedAt;
    }

    @JsonAutoDetect
    public static class BatchMemento implements Batchable {
        private long id;
        private long reagentId;
        private String label;
        private double quantityCurrent;
        private BatchUnit unit;
        private String location;
        private Instant expiresAt;
        private BatchStatus status;
        private Long ownerId;
        private Instant createdAt;
        private Instant updatedAt;

        public BatchMemento(long id, long reagentId, String label, double quantityCurrent, BatchUnit unit, String location, Instant expiresAt, BatchStatus status, Long ownerId, Instant createdAt, Instant updatedAt) {
            this.id = id;
            this.reagentId = reagentId;
            this.label = label;
            this.quantityCurrent = quantityCurrent;
            this.unit = unit;
            this.location = location;
            this.expiresAt = expiresAt;
            this.status = status;
            this.ownerId = ownerId;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
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

        public Long getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(Long ownerId) {
            this.ownerId = ownerId;
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
            return String.format("%-10s %-15s %-10s %-10s %-15s %-15s %-10s %-15s %-25s %-25s", reagentId, label, quantityCurrent, unit, location, StockUtils.formatterExp.format(expiresAt), status, ownerId, StockUtils.formatter.format(createdAt), StockUtils.formatter.format(updatedAt));
        }

    }
}
