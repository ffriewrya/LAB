package ru.itmo.moona.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import ru.itmo.moona.domain.Reagent;

import java.util.List;

public class ReagentTableController {

    @FXML
    private TableView<Reagent> table;

    public void setData(List<Reagent> data) {
        if (data.isEmpty()) return;
        table.setItems(FXCollections.observableArrayList(data));
    }
}
