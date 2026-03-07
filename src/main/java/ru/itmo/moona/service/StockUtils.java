package ru.itmo.moona.service;

import ru.itmo.moona.domain.BatchStatus;
import ru.itmo.moona.domain.BatchUnit;
import ru.itmo.moona.domain.StockMoveType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class StockUtils {
    private StockUtils() {
    }

    public static DateTimeFormatter formatterExp = DateTimeFormatter.ofPattern("d.MM.yyyy")
            .withZone(ZoneId.systemDefault());


    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d.MM.yyyy HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public static BatchUnit findUnit(String unit) {
        for (BatchUnit bu : BatchUnit.values()) {
            if (bu.name().equalsIgnoreCase(unit)) {
                return bu;
            }
        }
        throw new IllegalArgumentException("invalid unit. expected G or ML.");

    }

    public static BatchStatus findStatus(String status) {
        for (BatchStatus bs : BatchStatus.values()) {
            if (bs.name().equalsIgnoreCase(status)) {
                return bs;
            }
        }
        throw new IllegalArgumentException("invalid status. expected ACTIVE or ARCHIVED.");

    }

    public static StockMoveType findType(String type) {
        for (StockMoveType mt : StockMoveType.values()) {
            if (mt.name().equalsIgnoreCase(type)) {
                return mt;
            }
        }
        throw new IllegalArgumentException("invalid type. expected IN, OUT or DISCARD.");
    }

    public static Instant parseDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);
            LocalDateTime localDateTime = localDate.atStartOfDay();
            Instant finalDate = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            return finalDate;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("invalid date. expected dd-MM-yyyy");
        }
    }


}
