package ru.itmo.moona.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.itmo.moona.domain.BatchStatus;
import ru.itmo.moona.domain.BatchUnit;
import ru.itmo.moona.domain.Batchable;

import java.util.List;

import static ru.itmo.moona.service.StockUtils.formatter;
import static ru.itmo.moona.service.StockUtils.formatterExp;

public class BatchTableController {

    @FXML
    private TableView<Batchable> batchTable;

    @FXML
    private TableColumn<Batchable, Long> batchIdCol;
    @FXML
    private TableColumn<Batchable, Long> batchReagentIdCol;
    @FXML
    private TableColumn<Batchable, String> batchLabelCol;
    @FXML
    private TableColumn<Batchable, Double> batchQuantityCol;
    @FXML
    private TableColumn<Batchable, BatchUnit> batchUnitCol;
    @FXML
    private TableColumn<Batchable, String> batchLocationCol;
    @FXML
    private TableColumn<Batchable, String> batchExpiresAtCol;
    @FXML
    private TableColumn<Batchable, BatchStatus> batchStatusCol;
    @FXML
    private TableColumn<Batchable, String> batchOwnerUsernameCol;
    @FXML
    private TableColumn<Batchable, String> batchCreatedAtCol;
    @FXML
    private TableColumn<Batchable, String> batchUpdatedAtCol;

    public void setData(List<? extends Batchable> data) {
        if (data.isEmpty()) return;
        batchIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        batchReagentIdCol.setCellValueFactory(new PropertyValueFactory<>("reagentId"));
        batchLabelCol.setCellValueFactory(new PropertyValueFactory<>("label"));
        batchQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantityCurrent"));
        batchUnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
        batchLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        batchExpiresAtCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatterExp.format(cellData.getValue().getExpiresAt())));
        batchStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        batchOwnerUsernameCol.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));
        batchCreatedAtCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getCreatedAt())));
        batchUpdatedAtCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getUpdatedAt())));
        batchTable.setItems(FXCollections.observableArrayList(data));
    }
}
