package ru.itmo.moona.database;

import ru.itmo.moona.domain.Reagent;
import ru.itmo.moona.domain.ReagentBatch;

import javax.print.attribute.standard.MediaSize;
import java.sql.*;

public class BatchRepository {
    private final DatabaseManager manager;

    public BatchRepository(DatabaseManager manager) {
        this.manager = manager;
    }

    private Long save(ReagentBatch batch) throws SQLException {
        String sql = "INSERT INTO batches (reagent_id, label, quantity_current, batch_unit, location, expires_at, status, owner_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, batch.getReagentId());
            statement.setString(2, batch.getLabel());
            statement.setDouble(3, batch.getQuantityCurrent());
            statement.setObject(4, batch.getUnit(), Types.OTHER);
            statement.setString(5, batch.getLocation());
            statement.setObject(6, batch.getExpiresAt());
            statement.setObject(7, batch.getStatus(), Types.OTHER);
            statement.setLong(8, batch.getOwnerId());
            statement.setObject(9, batch.getCreatedAt());
            statement.setObject(10, batch.getUpdatedAt());

            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) return set.getLong(1);
            }
        }
        return null;
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
                statement.setObject(5, m.getUnit(), Types.OTHER);
                statement.setString(6, m.getLocation());
                statement.setObject(7, m.getExpiresAt());
                statement.setObject(8, m.getStatus(), Types.OTHER);
                statement.setLong(9, m.getOwnerId());
                statement.setObject(10, m.getCreatedAt());
                statement.setObject(11, m.getUpdatedAt());

                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}
