package ru.itmo.moona.database;

import ru.itmo.moona.domain.BatchStatus;
import ru.itmo.moona.domain.BatchUnit;
import ru.itmo.moona.domain.Reagent;
import ru.itmo.moona.domain.ReagentBatch;
import ru.itmo.moona.service.StockManager;

import javax.print.attribute.standard.MediaSize;
import javax.smartcardio.CommandAPDU;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BatchRepository {
    private final DatabaseManager manager;
    private final StockManager stockManager;

    public BatchRepository(DatabaseManager manager, StockManager stockManager) {
        this.manager = manager;
        this.stockManager = stockManager;
    }

    private Long save(ReagentBatch batch) throws SQLException {
        String sql = "INSERT INTO batches (reagent_id, label, quantity_current, batch_unit, location, expires_at, status, owner_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, batch.getReagentId());
            statement.setString(2, batch.getLabel());
            statement.setDouble(3, batch.getQuantityCurrent());
            statement.setObject(4, batch.getUnit().name(), Types.OTHER);
            statement.setString(5, batch.getLocation());
            statement.setTimestamp(6, java.sql.Timestamp.from(batch.getExpiresAt()));
            statement.setObject(7, batch.getStatus().name(), Types.OTHER);
            statement.setLong(8, batch.getOwnerId());
            statement.setTimestamp(9, java.sql.Timestamp.from(batch.getCreatedAt()));
            statement.setTimestamp(10, java.sql.Timestamp.from(batch.getUpdatedAt()));

            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) return set.getLong(1);
            }
        }
        return null;
    }

    public void updStatus(ReagentBatch b) throws SQLException {
        String sql = "UPDATE batches SET status = ?, updated_at = ? WHERE id = ?";
        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, b.getStatus().name(), Types.OTHER);
            statement.setTimestamp(2, java.sql.Timestamp.from(b.getUpdatedAt()));
            statement.setLong(3, b.getId());

            statement.executeUpdate();
        }
    }

    public void updLocation(ReagentBatch b) throws SQLException {
        String sql = "UPDATE batches SET location = ?, updated_at = ? WHERE id = ?";
        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, b.getLocation());
            statement.setTimestamp(2, java.sql.Timestamp.from(b.getUpdatedAt()));
            statement.setLong(3, b.getId());

            statement.executeUpdate();
        }
    }

    public void updExpiresAt(ReagentBatch b) throws SQLException {
        String sql = "UPDATE batches SET expires_at = ?, updated_at = ? WHERE id = ?";
        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, java.sql.Timestamp.from(b.getExpiresAt()));
            statement.setTimestamp(2, java.sql.Timestamp.from(b.getUpdatedAt()));
            statement.setLong(3, b.getId());

            statement.executeUpdate();
        }
    }

    public void updLabel(ReagentBatch b) throws SQLException {
        String sql = "UPDATE batches SET label = ?, updated_at = ? WHERE id = ?";
        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, b.getLabel());
            statement.setTimestamp(2, java.sql.Timestamp.from(b.getUpdatedAt()));
            statement.setLong(3, b.getId());

            statement.executeUpdate();
        }
    }

    public void updQuantity(long id) throws SQLException {
        String sql = "UPDATE batches SET quantity_current = ?, updated_at = ? WHERE id = ?";
        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ReagentBatch b = stockManager.getBatch(id);
            statement.setDouble(1, b.getQuantityCurrent());
            statement.setTimestamp(2, java.sql.Timestamp.from(b.getUpdatedAt()));
            statement.setLong(3, b.getId());

            statement.executeUpdate();
        }
    }

    public void fullUpdate(ReagentBatch b) throws SQLException {
        updLabel(b);
        updLocation(b);
        updExpiresAt(b);
        updStatus(b);
    }

    public void addMemento(ReagentBatch.BatchMemento m) throws SQLException {
        String sql = "INSERT INTO batch_memento (batch_id, reagent_id, label, quantity_current, batch_unit, location, expires_at, status, owner_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, m.getId());
            statement.setLong(2, m.getReagentId());
            statement.setString(3, m.getLabel());
            statement.setDouble(4, m.getQuantityCurrent());
            statement.setObject(5, m.getUnit().name(), Types.OTHER);
            statement.setString(6, m.getLocation());
            statement.setTimestamp(7, Timestamp.from(m.getExpiresAt()));
            statement.setObject(8, m.getStatus().name(), Types.OTHER);
            statement.setLong(9, m.getOwnerId());
            statement.setTimestamp(10, Timestamp.from(m.getCreatedAt()));
            statement.setTimestamp(11, Timestamp.from(m.getUpdatedAt()));

            statement.executeUpdate();

        }
    }

    private List<ReagentBatch.BatchMemento> getMemento(ReagentBatch b) throws SQLException {
        List<ReagentBatch.BatchMemento> result = new ArrayList<>();
        String sql = "SELECT * FROM batch_memento WHERE batch_id = ?";
        ;
        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, b.getId());

            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    ReagentBatch.BatchMemento m = new ReagentBatch.BatchMemento(set.getLong("batch_id"), set.getLong("reagent_id"), set.getString("label"), set.getDouble("quantity_current"), BatchUnit.valueOf(set.getString("batch_unit")), set.getString("location"), set.getTimestamp("expires_at").toInstant(), BatchStatus.valueOf(set.getString("status")), set.getLong("owner_id"), set.getTimestamp("created_at").toInstant(), set.getTimestamp("updated_at").toInstant());
                    result.add(m);
                }
            }
        }
        return result;
    }

    public HashMap<Long, ReagentBatch> getBatches() throws SQLException {
        HashMap<Long, ReagentBatch> result = new HashMap();
        String sql = "SELECT * FROM batches";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet set = statement.executeQuery()) {
            while (set.next()) {
                ReagentBatch b = new ReagentBatch.BatchBuilder()
                        .setId(set.getLong("id"))
                        .setReagentId(set.getLong("reagent_id"))
                        .setLabel(set.getString("label"))
                        .setQuantityCurrent(set.getDouble("quantity_current"))
                        .setUnit(BatchUnit.valueOf(set.getString("batch_unit")))
                        .setLocation(set.getString("location"))
                        .setExpiresAt(set.getTimestamp("expires_at").toInstant())
                        .setStatus(BatchStatus.valueOf(set.getString("status")))
                        .setOwnerId(set.getLong("owner_id"))
                        .setCreatedAt(set.getTimestamp("created_at").toInstant())
                        .setUpdatedAt(set.getTimestamp("updated_at").toInstant())
                        .build();
                b.setHistory(getMemento(b));
                result.put(b.getId(), b);
            }
        }
        return result;
    }

    public void saveBatchWithMemento(ReagentBatch batch) throws SQLException {
        Long id = save(batch);
        batch.setId(id);
        String sql = "INSERT INTO batch_memento (batch_id, reagent_id, label, quantity_current, batch_unit, location, expires_at, status, owner_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (ReagentBatch.BatchMemento m : batch.getHistory()) {
                statement.setLong(1, m.getId());
                statement.setLong(2, m.getReagentId());
                statement.setString(3, m.getLabel());
                statement.setDouble(4, m.getQuantityCurrent());
                statement.setObject(5, m.getUnit().name(), Types.OTHER);
                statement.setString(6, m.getLocation());
                statement.setTimestamp(7, java.sql.Timestamp.from(m.getExpiresAt()));
                statement.setObject(8, m.getStatus().name(), Types.OTHER);
                statement.setLong(9, m.getOwnerId());
                statement.setTimestamp(10, java.sql.Timestamp.from(m.getCreatedAt()));
                statement.setTimestamp(11, java.sql.Timestamp.from(m.getUpdatedAt()));

                statement.addBatch();
            }
            statement.executeBatch();
        }
    }


    public void deleteBatch(ReagentBatch b) throws SQLException {
        String sql = "DELETE FROM batches WHERE id = ?";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, b.getId());
            statement.executeUpdate();
        }
    }
}
