package ru.itmo.moona.database;

import ru.itmo.moona.domain.Reagent;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReagentRepository {

    private final DatabaseManager manager;

    public ReagentRepository(DatabaseManager manager) {
        this.manager = manager;
    }

    public void save(Reagent reagent) throws SQLException {
        String sql = "INSERT INTO reagents (name, formula, cas, hazard_class, owner_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reagent.getName());
            statement.setString(2, reagent.getFormula());
            statement.setString(3, reagent.getCas());
            statement.setString(4, reagent.getHazardClass());
            statement.setLong(5, reagent.getOwnerId());
            statement.setObject(6, reagent.getCreatedAt());
            statement.setObject(7, reagent.getUpdatedAt());

            statement.executeUpdate();
        }
    }

    public List<Reagent> getReagents() throws SQLException {
        List<Reagent> result = new ArrayList<>();
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
                        .setCreatedAt(set.getObject("created_at", Instant.class))
                        .setUpdatedAt(set.getObject("updated_at", Instant.class))
                        .build();
                result.add(b);
            }
        }
        return result;
    }
}
