package ru.itmo.moona.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.itmo.moona.domain.BatchStatus;
import ru.itmo.moona.domain.Batchable;

import java.util.List;

import static ru.itmo.moona.service.StockUtils.formatter;
import static ru.itmo.moona.service.StockUtils.formatterExp;

public class MementoController {

    @FXML
    private TableView<Batchable> historyTable;

    @FXML
    private TableColumn<Batchable, String> batchLabelCol;
    @FXML
    private TableColumn<Batchable, Double> batchQuantityCol;
    @FXML
    private TableColumn<Batchable, String> batchLocationCol;
    @FXML
    private TableColumn<Batchable, String> batchExpiresAtCol;
    @FXML
    private TableColumn<Batchable, BatchStatus> batchStatusCol;
    @FXML
    private TableColumn<Batchable, String> batchUpdatedAtCol;

    private static final String MATCHED_STYLE = "-fx-text-fill: #d3d8db;";
    private static final String FINAL_STYLE = "-fx-font-weight: bold;";

    public void setData(List<? extends Batchable> data) {
        if (data.isEmpty()) return;
        batchLabelCol.setCellValueFactory(new PropertyValueFactory<>("label"));
        batchQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantityCurrent"));
        batchLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        batchExpiresAtCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatterExp.format(cellData.getValue().getExpiresAt())));
        batchStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        batchUpdatedAtCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getUpdatedAt())));

        batchLabelCol.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    int cur = getIndex();
                    int last = data.size() - 1;
                    if (cur != 0 && cur != last) {
                        Batchable memento = data.get(cur);
                        Batchable prev = data.get(cur - 1);
                        if (memento.getLabel().equals(prev.getLabel())) {
                            setStyle(MATCHED_STYLE);
                        } else {
                            setStyle("");
                        }
                    }
                    else {
                        setStyle(FINAL_STYLE);
                    }
                }
            }
        });

        batchQuantityCol.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    int cur = getIndex();
                    int last = data.size() - 1;
                    if (cur != 0 && cur != last) {
                        Batchable memento = data.get(cur);
                        Batchable prev = data.get(cur - 1);
                        if (memento.getQuantityCurrent() == (prev.getQuantityCurrent())) {
                            setStyle(MATCHED_STYLE);
                        } else {
                            setStyle("");
                        }
                    }
                    else {
                        setStyle(FINAL_STYLE);
                    }
                }
            }
        });

        batchLocationCol.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    int cur = getIndex();
                    int last = data.size() - 1;
                    if (cur != 0 && cur != last) {
                        Batchable memento = data.get(cur);
                        Batchable prev = data.get(cur - 1);
                        if (memento.getLocation().equals(prev.getLocation())) {
                            setStyle(MATCHED_STYLE);
                        } else {
                            setStyle("");
                        }
                    }
                    else {
                        setStyle(FINAL_STYLE);
                    }
                }
            }
        });

        batchExpiresAtCol.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    int cur = getIndex();
                    int last = data.size() - 1;
                    if (cur != 0 && cur != last) {
                        Batchable memento = data.get(cur);
                        Batchable prev = data.get(cur - 1);
                        if (memento.getExpiresAt().equals(prev.getExpiresAt())) {
                            setStyle(MATCHED_STYLE);
                        } else {
                            setStyle("");
                        }
                    }
                    else {
                        setStyle(FINAL_STYLE);
                    }
                }
            }
        });

        batchStatusCol.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(BatchStatus item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.name());
                    int cur = getIndex();
                    int last = data.size() - 1;
                    if (cur != 0 && cur != last) {
                        Batchable memento = data.get(cur);
                        Batchable prev = data.get(cur - 1);
                        if (memento.getStatus().equals(prev.getStatus())) {
                            setStyle(MATCHED_STYLE);
                        } else {
                            setStyle("");
                        }
                    }
                    else {
                        setStyle(FINAL_STYLE);
                    }
                }
            }
        });

        batchUpdatedAtCol.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    int cur = getIndex();
                    int last = data.size() - 1;
                    if (cur == 0 || cur == last) {
                            setStyle(FINAL_STYLE);
                    }
                    else {
                        setStyle("");
                    }
                }
            }
        });



        historyTable.setItems(FXCollections.observableArrayList(data));
    }

}
