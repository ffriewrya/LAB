package ru.itmo.moona.database;

import ru.itmo.moona.domain.Reagent;
import ru.itmo.moona.domain.ReagentBatch;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReagentRepository {

    private final DatabaseManager manager;

    public ReagentRepository(DatabaseManager manager) {
        this.manager = manager;
    }

    public void save(Reagent reagent) throws SQLException {
        String sql = "INSERT INTO reagents (name, formula, cas, hazard_class, owner_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reagent.getName());
            statement.setString(2, reagent.getFormula());
            statement.setString(3, reagent.getCas());
            statement.setString(4, reagent.getHazardClass());
            statement.setLong(5, reagent.getOwnerId());
            statement.setTimestamp(6, java.sql.Timestamp.from(reagent.getCreatedAt()));
            statement.setObject(7, java.sql.Timestamp.from(reagent.getUpdatedAt()));

            try (ResultSet set = statement.executeQuery()) {
                if (set.next())  reagent.setId(set.getLong(1));
            }
        }
    }


    public HashMap<Long, Reagent> getReagents() throws SQLException {
        HashMap<Long, Reagent> result = new HashMap<>();
        String sql = "SELECT * FROM reagents";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet set = statement.executeQuery()) {
            while (set.next()) {
                Reagent b = new Reagent.ReagentBuilder()
                        .setId(set.getLong("id"))
                        .setName(set.getString("name"))
                        .setFormula(set.getString("formula"))
                        .setCas(set.getString("cas"))
                        .setHazardClass(set.getString("hazard_class"))
                        .setOwnerId(set.getLong("owner_id"))
                        .setCreatedAt(set.getTimestamp("created_at").toInstant())
                        .setUpdatedAt(set.getTimestamp("updated_at").toInstant())
                        .build();
                result.put(b.getId(), b);
            }
        }
        return result;
    }

    public void deleteReagent(Reagent r) throws SQLException {
        String sql = "DELETE FROM reagents WHERE id = ?";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, r.getId());
            statement.executeUpdate();

        }
    }
}
