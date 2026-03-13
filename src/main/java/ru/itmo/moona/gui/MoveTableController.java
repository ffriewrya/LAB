package ru.itmo.moona.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.itmo.moona.domain.BatchUnit;
import ru.itmo.moona.domain.StockMove;
import ru.itmo.moona.domain.StockMoveType;

import java.util.List;

import static ru.itmo.moona.service.StockUtils.formatter;
import static ru.itmo.moona.service.StockUtils.formatterExp;

public class MoveTableController {
    @FXML
    private TableView<StockMove> moveTable;

    @FXML
    private TableColumn<StockMove, Long> moveIdCol;
    @FXML
    private TableColumn<StockMove, Long> moveBatchIdCol;
    @FXML
    private TableColumn<StockMove, StockMoveType> moveTypeCol;
    @FXML
    private TableColumn<StockMove, Double> moveQuantityCol;
    @FXML
    private TableColumn<StockMove, BatchUnit> moveUnitCol;
    @FXML
    private TableColumn<StockMove, String> moveReasonCol;
    @FXML
    private TableColumn<StockMove, String> moveOwnerUsernameCol;
    @FXML
    private TableColumn<StockMove, String> moveMovedAtCol;
    @FXML
    private TableColumn<StockMove, String> moveCreatedAtCol;

    public void setData(List<StockMove> data) {
        if (data.isEmpty()) return;
        moveIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        moveBatchIdCol.setCellValueFactory(new PropertyValueFactory<>("batchId"));
        moveTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        moveQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        moveUnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
        moveReasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        moveOwnerUsernameCol.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));
        moveMovedAtCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatterExp.format(cellData.getValue().getMovedAt())));
        moveCreatedAtCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getCreatedAt())));

        moveTable.setItems(FXCollections.observableArrayList(data));
    }
}
