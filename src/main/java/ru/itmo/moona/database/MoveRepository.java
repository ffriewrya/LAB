package ru.itmo.moona.database;

import ru.itmo.moona.domain.BatchUnit;
import ru.itmo.moona.domain.StockMove;
import ru.itmo.moona.domain.StockMoveType;
import ru.itmo.moona.service.StockManager;

import java.sql.*;
import java.util.HashMap;

public class MoveRepository {
    private final DatabaseManager manager;

    public MoveRepository(DatabaseManager manager) {
        this.manager = manager;
    }

    public void save(StockMove move) throws SQLException {
        String sql = "INSERT INTO moves (batch_id, type, quantity, unit, reason, owner_id, moved_at, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, move.getBatchId());
            statement.setObject(2, move.getType().name(), Types.OTHER); // Для Enum, используем .name() и Types.OTHER
            statement.setDouble(3, move.getQuantity());
            statement.setObject(4, move.getUnit().name(), Types.OTHER); // Для Enum
            statement.setString(5, move.getReason());
            statement.setLong(6, move.getOwnerId());
            statement.setTimestamp(7, Timestamp.from(move.getMovedAt())); // Instant в Timestamp
            statement.setTimestamp(8, Timestamp.from(move.getCreatedAt()));

            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    move.setId(set.getLong(1));
                }
            }
        }
    }

    public HashMap<Long, StockMove> getMoves() throws SQLException {
        HashMap<Long, StockMove> result = new HashMap<>();
        String sql = "SELECT id, batch_id, type, quantity, unit, reason, owner_id, moved_at, created_at FROM moves";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet set = statement.executeQuery()) {
            while (set.next()) {
                StockMove move = new StockMove.MoveBuilder()
                        .setId(set.getLong("id"))
                        .setBatchId(set.getLong("batch_id"))
                        .setType(StockMoveType.valueOf(set.getString("type")))
                        .setQuantity(set.getDouble("quantity"))
                        .setUnit(BatchUnit.valueOf(set.getString("unit")))
                        .setReason(set.getString("reason"))
                        .setOwnerId(set.getLong("owner_id"))
                        .setMovedAt(set.getTimestamp("moved_at").toInstant())
                        .setCreatedAt(set.getTimestamp("created_at").toInstant())
                        .build();
                result.put(move.getId(), move);
            }
        }
        return result;
    }

    public void deleteMove(StockMove move) throws SQLException {
        String sql = "DELETE FROM moves WHERE id = ?";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, move.getId());
            statement.executeUpdate();
        }
    }
}
