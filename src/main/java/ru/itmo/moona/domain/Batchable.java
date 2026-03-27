package ru.itmo.moona.domain;

import java.time.Instant;

public interface Batchable {
    long getId();

    long getReagentId();

    String getLabel();

    double getQuantityCurrent();

    BatchUnit getUnit();

    String getLocation();

    Instant getExpiresAt();

    BatchStatus getStatus();

    Long getOwnerId();

    Instant getCreatedAt();

    Instant getUpdatedAt();
}
