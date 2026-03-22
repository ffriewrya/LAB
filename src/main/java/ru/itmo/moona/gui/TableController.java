package ru.itmo.moona.gui;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.itmo.moona.domain.*;
import ru.itmo.moona.domain.users.CurrentUser;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.storage.FileStorage;
import ru.itmo.moona.storage.StockSnapshot;
import ru.itmo.moona.storage.StockValidator;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import static ru.itmo.moona.service.StockUtils.formatter;
import static ru.itmo.moona.service.StockUtils.formatterExp;


public class TableController {
    @FXML
    private TableView<Reagent> reagentTable;
    @FXML
    private TableColumn<Reagent, Long> reagentIdCol;
    @FXML
    private TableColumn<Reagent, String> reagentNameCol;
    @FXML
    private TableColumn<Reagent, String> reagentFormulaCol;
    @FXML
    private TableColumn<Reagent, String> reagentCasCol;
    @FXML
    private TableColumn<Reagent, String> reagentHazardClassCol;
    @FXML
    private TableColumn<Reagent, String> reagentOwnerUsernameCol;
    @FXML
    private TableColumn<Reagent, String> reagentCreatedAtCol;
    @FXML
    private TableColumn<Reagent, String> reagentUpdatedAtCol;

    @FXML
    private TableView<ReagentBatch> batchTable;
    @FXML
    private TableColumn<ReagentBatch, Long> batchIdCol;
    @FXML
    private TableColumn<ReagentBatch, Long> batchReagentIdCol;
    @FXML
    private TableColumn<ReagentBatch, String> batchLabelCol;
    @FXML
    private TableColumn<ReagentBatch, Double> batchQuantityCol;
    @FXML
    private TableColumn<ReagentBatch, BatchUnit> batchUnitCol;
    @FXML
    private TableColumn<ReagentBatch, String> batchLocationCol;
    @FXML
    private TableColumn<ReagentBatch, String> batchExpiresAtCol;
    @FXML
    private TableColumn<ReagentBatch, BatchStatus> batchStatusCol;
    @FXML
    private TableColumn<ReagentBatch, String> batchOwnerUsernameCol;
    @FXML
    private TableColumn<ReagentBatch, String> batchCreatedAtCol;
    @FXML
    private TableColumn<ReagentBatch, String> batchUpdatedAtCol;

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
    @FXML
    private Label undoRedo;


    private HashMap<Long, Reagent> reagents;
    private HashMap<Long, ReagentBatch> batches;
    private HashMap<Long, StockMove> moves;
    private StockManager manager;
    private FileStorage storage;
    private StockValidator validator;

    private ObservableList<Reagent> r;
    private ObservableList<ReagentBatch> b;
    private ObservableList<StockMove> m;

    private final Stack<UndoRedoManager> undoStack = new Stack<>();
    private final Stack<UndoRedoManager> redoStack = new Stack<>();

    public void setValidator(StockValidator validator) {
        this.validator = validator;
    }

    public void setStorage(FileStorage storage) {
        this.storage = storage;
    }

    public void setManager(StockManager manager) {
        this.manager = manager;
    }

    public void setReagents(HashMap<Long, Reagent> reagents) {
        this.reagents = reagents;
    }

    public void setBatches(HashMap<Long, ReagentBatch> batches) {
        this.batches = batches;
    }

    public void setMoves(HashMap<Long, StockMove> moves) {
        this.moves = moves;
    }

    private class UndoRedoManager {
        private Runnable undo;
        private Runnable redo;
        private String description;

        public UndoRedoManager(Runnable undo, Runnable redo, String description) {
            this.undo = undo;
            this.redo = redo;
            this.description = description;
        }

        private void undo() {
            undo.run();
        }

        private void redo() {
            redo.run();
        }

        public String getDescription() {
            return description;
        }
    }

    public void initializeTable() {
        r = FXCollections.observableArrayList(reagents.values());
        b = FXCollections.observableArrayList(batches.values());
        m = FXCollections.observableArrayList(moves.values());

        reagentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        reagentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        reagentFormulaCol.setCellValueFactory(new PropertyValueFactory<>("formula"));
        reagentCasCol.setCellValueFactory(new PropertyValueFactory<>("cas"));
        reagentHazardClassCol.setCellValueFactory(new PropertyValueFactory<>("hazardClass"));
        reagentOwnerUsernameCol.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));
        reagentCreatedAtCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getCreatedAt())));
        reagentUpdatedAtCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getUpdatedAt())));

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

        moveIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        moveBatchIdCol.setCellValueFactory(new PropertyValueFactory<>("batchId"));
        moveTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        moveQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        moveUnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
        moveReasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        moveOwnerUsernameCol.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));
        moveMovedAtCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatterExp.format(cellData.getValue().getMovedAt())));
        moveCreatedAtCol.setCellValueFactory(cellData -> new SimpleStringProperty(formatter.format(cellData.getValue().getCreatedAt())));

        reagentTable.setItems(r);
        batchTable.setItems(b);
        moveTable.setItems(m);

        reagentTable.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, e -> {
            if (batchTable.getSelectionModel().getSelectedItem() == null) {
                e.consume();
            }
        });

        batchTable.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, e -> {
            if (batchTable.getSelectionModel().getSelectedItem() == null) {
                e.consume();
            }
        });
    }

    @FXML
    public void initialize() {
        if (reagents != null && batches != null && moves != null) {
            initializeTable();

        }
    }

    private void newBatchWindow(List<? extends Batchable> data, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../batchTable.fxml"));
        Parent root = loader.load();
        BatchTableController controller = loader.getController();
        controller.setData(data);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    private void newMoveWindow(List<StockMove> data) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../moveTable.fxml"));
        Parent root = loader.load();
        MoveTableController controller = loader.getController();
        controller.setData(data);
        Stage stage = new Stage();
        stage.setTitle("moves");
        stage.setScene(new Scene(root));

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    private void newReagentWindow(List<Reagent> data) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(""));
        Parent root = loader.load();
        ReagentTableController controller = loader.getController();
        controller.setData(data);
        Stage stage = new Stage();
        stage.setTitle("reagents");
        stage.setScene(new Scene(root));

        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    @FXML
    private void handleRefresh() {
        this.reagents = manager.getReagents();
        this.batches = manager.getBatches();
        this.moves = manager.getMoves();

        r.setAll(reagents.values());
        b.setAll(batches.values());
        m.setAll(moves.values());

    }

    @FXML
    private void handleArchive() {
        try {
            ReagentBatch sel = batchTable.getSelectionModel().getSelectedItem();
            ReagentBatch.BatchMemento oldState = sel.createMemento();
            Long id = sel.getId();
            manager.archiveBatch(id);
            ReagentBatch.BatchMemento newState = sel.createMemento();
            sel.addMemento(newState);

            Runnable undoAction = () -> {
                sel.restoreStatusFromMemento(oldState);
            };

            Runnable redoAction = () -> {
                sel.restoreStatusFromMemento(newState);
            };

            UndoRedoManager m = new UndoRedoManager(undoAction, redoAction, "archivation");
            undoStack.push(m);
            redoStack.clear();
            showSuccess("batch archived");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleBatchHistory() {
        ReagentBatch sel = batchTable.getSelectionModel().getSelectedItem();
        Long id = sel.getId();
        try {
            newBatchWindow(sel.getHistory(), "batch history");
        } catch (IOException e) {
            showError("can't display batch history");
        }
    }

    @FXML
    private void handleBatchList() {
        Reagent sel = reagentTable.getSelectionModel().getSelectedItem();
        Long id = sel.getId();
        try {
            newBatchWindow(manager.findBatch(id), "batches");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleMoveList() {
        ReagentBatch sel = batchTable.getSelectionModel().getSelectedItem();
        Long id = sel.getId();
        try {
            newMoveWindow(manager.showMoves(id));
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleReagentList(String name) {
        try {
            newReagentWindow(manager.findReagent(name));
        } catch (IOException e) {
            showError("can't display reagent list");
        }
    }

    private void showError(String e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("error!");
        alert.setContentText(e);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    private void showSuccess(String s, long id) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("success!");
        alert.setContentText(s + id);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    private void showSuccess(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("success!");
        alert.setContentText(s);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    @FXML
    private void handleAddReagent() {
        Dialog<Reagent> dialog = new Dialog<>();
        dialog.setTitle("creating a reagent");
        dialog.setHeaderText("input your reagent data");

        ButtonType btn = new ButtonType("add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btn, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);


        TextField name = new TextField();
        TextField formula = new TextField();
        formula.setPromptText("optional");
        TextField cas = new TextField();
        cas.setPromptText("optional");
        TextField hz = new TextField();
        hz.setPromptText("optional");


        g.add(new Label("name"), 0, 0);
        g.add(name, 1, 0);
        g.add(new Label("formula"), 0, 1);
        g.add(formula, 1, 1);
        g.add(new Label("CAS"), 0, 2);
        g.add(cas, 1, 2);
        g.add(new Label("hazard class"), 0, 3);
        g.add(hz, 1, 3);

        dialog.getDialogPane().setContent(g);

        dialog.setResultConverter(clickedButton -> {
            if (clickedButton == ButtonType.CANCEL) {
                return null;
            }
            try {
                return new Reagent.ReagentBuilder()
                        .setId(manager.genReagentId())
                        .setCreatedAt(Instant.now())
                        .setUpdatedAt(Instant.now())
                        .setOwnerId(CurrentUser.getInstance().getUser().getId())
                        .setName(name.getText())
                        .setFormula(formula.getText())
                        .setCas(cas.getText())
                        .setHazardClass(hz.getText())
                        .build();
            } catch (Exception e) {
                showError(e.getMessage());
            }
            return null;
        });

        Optional<Reagent> result = dialog.showAndWait();
        result.ifPresent(newReagent -> {
            manager.addReagent(newReagent);

            Runnable undoAction = () -> {
                manager.removeReagent(newReagent);
            };

            Runnable redoAction = () -> {
                manager.redoReagent(newReagent);
            };

            UndoRedoManager mng = new UndoRedoManager(undoAction, redoAction, "adding reagent");
            undoStack.push(mng);
            redoStack.clear();
            showSuccess("successfully added a reagent ", newReagent.getId());
        });
    }

    @FXML
    private void handleAddBatch() {
        Dialog<ReagentBatch> dialog = new Dialog<>();
        dialog.setTitle("creating a batch");
        dialog.setHeaderText("input your batch data");

        ButtonType btn = new ButtonType("add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btn, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);


        ComboBox<Reagent> rgs = new ComboBox<>();
        rgs.setPromptText("available reagents");
        rgs.setItems(FXCollections.observableArrayList(reagents.values()));

        TextField label = new TextField();

        //
        TextField quantity = new TextField();

        ComboBox<BatchUnit> unitBox = new ComboBox<>();
        unitBox.setItems(FXCollections.observableArrayList(BatchUnit.values()));
        unitBox.setValue(BatchUnit.G);


        TextField location = new TextField();

        ComboBox<BatchStatus> statusBox = new ComboBox<>();
        statusBox.setItems(FXCollections.observableArrayList(BatchStatus.values()));
        statusBox.setValue(BatchStatus.ACTIVE);

        DatePicker expiresAt = new DatePicker();
        expiresAt.setValue(LocalDate.now().plusYears(1));


        g.add(new Label("reagent"), 0, 0);
        g.add(rgs, 1, 0);
        g.add(new Label("label"), 0, 1);
        g.add(label, 1, 1);
        g.add(new Label("quantity"), 0, 2);
        g.add(quantity, 1, 2);
        g.add(new Label("unit"), 0, 3);
        g.add(unitBox, 1, 3);
        g.add(new Label("location"), 0, 4);
        g.add(location, 1, 4);
        g.add(new Label("status"), 0, 5);
        g.add(statusBox, 1, 5);
        g.add(new Label("expiresAt"), 0, 6);
        g.add(expiresAt, 1, 6);

        dialog.getDialogPane().setContent(g);

        dialog.setResultConverter(clickedButton -> {
            if (clickedButton == ButtonType.CANCEL) {
                return null;
            }
            try {
                Double parsedQ;
                try {
                    parsedQ = Double.parseDouble(quantity.getText());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("invalid quantity. expected a number");
                }
                if (rgs.getValue() == null) {
                    throw new IllegalArgumentException("reagentId can't be null");
                }
                return new ReagentBatch.BatchBuilder()
                        .setId(manager.genBatchId())
                        .setCreatedAt()
                        .setUpdatedAt()
                        .setOwnerId(CurrentUser.getInstance().getUser().getId())
                        .setReagentId(rgs.getValue().getId())
                        .setLabel(label.getText())
                        .setQuantityCurrent(parsedQ)
                        .setUnit(unitBox.getValue())
                        .setLocation(location.getText())
                        .setStatus(statusBox.getValue())
                        .setExpiresAt(expiresAt.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
                        .build();
            } catch (Exception e) {
                showError(e.getMessage());
            }
            return null;
        });

        Optional<ReagentBatch> result = dialog.showAndWait();
        result.ifPresent(newBatch -> {
            manager.addBatch(newBatch);

            Runnable undoAction = () -> {
                manager.removeBatch(newBatch);
            };

            Runnable redoAction = () -> {
                manager.redoBatch(newBatch);
            };

            UndoRedoManager mng = new UndoRedoManager(undoAction, redoAction, "adding batch");
            undoStack.push(mng);
            redoStack.clear();
            showSuccess("successfully added a batch ", newBatch.getId());
        });
    }

    @FXML
    private void handleAddMove() {
        Dialog<StockMove> dialog = new Dialog<>();
        dialog.setTitle("creating a move");
        dialog.setHeaderText("input your move data");

        ButtonType btn = new ButtonType("add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btn, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);


        ComboBox<ReagentBatch> batchComboBox = new ComboBox<>();
        batchComboBox.setPromptText("available batches");
        batchComboBox.setItems(FXCollections.observableArrayList(manager.findActiveBatches()));

        ComboBox<StockMoveType> typeComboBox = new ComboBox<>();
        typeComboBox.setItems(FXCollections.observableArrayList(StockMoveType.values()));
        typeComboBox.setValue(StockMoveType.IN);

        TextField quantity = new TextField();

        TextField reason = new TextField();
        reason.setPromptText("optional");

        DatePicker movingDate = new DatePicker();
        movingDate.setValue(LocalDate.now());

        g.add(new Label("batch"), 0, 0);
        g.add(batchComboBox, 1, 0);
        g.add(new Label("type"), 0, 1);
        g.add(typeComboBox, 1, 1);
        g.add(new Label("quantity"), 0, 2);
        g.add(quantity, 1, 2);
        g.add(new Label("reason"), 0, 3);
        g.add(reason, 1, 3);
        g.add(new Label("moving date"), 0, 4);
        g.add(movingDate, 1, 4);

        dialog.getDialogPane().setContent(g);

        dialog.setResultConverter(clickedButton -> {
            if (clickedButton == ButtonType.CANCEL) {
                return null;
            }
            try {
                Double parsedQ;
                try {
                    parsedQ = Double.parseDouble(quantity.getText());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("invalid quantity. expected a number");
                }
                if (batchComboBox.getValue() == null) {
                    throw new IllegalArgumentException("reagentId can't be null");
                }
                StockMove.MoveBuilder builder = new StockMove.MoveBuilder();
                builder.setId(manager.genMoveId())
                        .setCreatedAt()
                        .setOwnerId(CurrentUser.getInstance().getUser().getId())
                        .setBatchId(batchComboBox.getValue().getId())
                        .setUnit(manager.setMoveUnit(batchComboBox.getValue().getId()))
                        .setType(typeComboBox.getValue())
                        .setQuantity(parsedQ)
                        .setReason(reason.getText())
                        .setMovedAt(movingDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                StockMove created = builder.build();
                if (created.getType() == StockMoveType.IN) {
                    manager.moveIn(created);
                } else {
                    manager.moveOutDiscard(created);
                }
                return created;
            } catch (Exception e) {
                showError(e.getMessage());
            }
            return null;
        });

        Optional<StockMove> result = dialog.showAndWait();
        result.ifPresent(newMove -> {
            manager.addMove(newMove);

            Runnable undoAction = () -> {
                manager.removeMove(newMove);
                if (newMove.getType() == StockMoveType.IN) {
                    manager.moveOutDiscard(newMove);
                } else {
                    manager.moveIn(newMove);
                }
            };

            Runnable redoAction = () -> {
                manager.redoMove(newMove);
                if (newMove.getType() == StockMoveType.IN) {
                    manager.moveIn(newMove);
                } else {
                    manager.moveOutDiscard(newMove);
                }
            };

            UndoRedoManager mng = new UndoRedoManager(undoAction, redoAction, "adding move");
            undoStack.push(mng);
            redoStack.clear();
            showSuccess("successfully added a move ", newMove.getId());
        });
    }


    @FXML
    private void handleSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("saving to JSON");
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = chooser.showSaveDialog(reagentTable.getScene().getWindow());

        if (file != null) {
            try {
                storage.saveToJson(file.getAbsolutePath());
                showSuccess("successfully saved stock");
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }


    @FXML
    private void handleLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Stock Snapshot");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        File file = fileChooser.showOpenDialog(reagentTable.getScene().getWindow());

        if (file != null) {
            try {
                StockSnapshot oldState = storage.save();
                StockSnapshot snapshot = storage.loadFromJson(file.getAbsolutePath());
                validator.validate(snapshot);
                manager.loadStock(snapshot);

                Runnable undoAction = () -> {
                    manager.loadStock(oldState);
                };

                Runnable redoAction = () -> {
                    manager.loadStock(snapshot);
                };

                UndoRedoManager mng = new UndoRedoManager(undoAction, redoAction, "loading from JSON");
                undoStack.push(mng);
                redoStack.clear();

                showSuccess("successfully loaded stock");
            } catch (Exception e) {
                showError(e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdateBatch() {
        Dialog<ReagentBatch> dialog = new Dialog<>();
        dialog.setTitle("updating a batch");
        dialog.setHeaderText("input your batch data");

        ButtonType btn = new ButtonType("add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btn, ButtonType.CANCEL);
        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);

        TextField location = new TextField();
        DatePicker expiresAt = new DatePicker();
        ComboBox<BatchStatus> statusBox = new ComboBox<>();
        statusBox.setItems(FXCollections.observableArrayList(BatchStatus.values()));
        TextField label = new TextField();

        g.add(new Label("location"), 0, 0);
        g.add(location, 1, 0);
        g.add(new Label("expiresAt"), 0, 1);
        g.add(expiresAt, 1, 1);
        g.add(new Label("status"), 0, 2);
        g.add(statusBox, 1, 2);
        g.add(new Label("label"), 0, 3);
        g.add(label, 1, 3);

        dialog.getDialogPane().setContent(g);

        dialog.setResultConverter(clickedButton -> {
            if (clickedButton == ButtonType.CANCEL) {
                return null;
            }
            try {
                if (location.getText().isBlank() && expiresAt.getValue() == null && statusBox.getValue() == null && label.getText().isBlank()) {
                    return null;
                }
                ReagentBatch sel = batchTable.getSelectionModel().getSelectedItem();
                return sel;
            } catch (Exception e) {
                showError(e.getMessage());
            }
            return null;
        });

        Optional<ReagentBatch> result = dialog.showAndWait();
        result.ifPresent(batch -> {
            try {
                ReagentBatch.BatchMemento oldState = batch.createMemento();
                if (!location.getText().isBlank()) {
                    manager.updLocation(batch.getId(), location.getText());
                }
                if (expiresAt.getValue() != null) {
                    manager.updExpiresAt(batch.getId(), expiresAt.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                }
                if (statusBox.getValue() != null) {
                    manager.updStatus(batch.getId(), statusBox.getValue());
                }
                if (!label.getText().isBlank()) {
                    manager.updLabel(batch.getId(), label.getText());
                }
                ReagentBatch.BatchMemento newState = batch.createMemento();
                batch.addMemento(newState);

                Runnable undoAction = () -> {
                    batch.restoreFromMemento(oldState);
                };

                Runnable redoAction = () -> {
                    batch.restoreFromMemento(newState);
                };

                UndoRedoManager mng = new UndoRedoManager(undoAction, redoAction, "updating batch");
                undoStack.push(mng);
                redoStack.clear();
            } catch (Exception e) {
                showError(e.getMessage());
            }

            showSuccess("successfully updated batch " + batch.getId());
        });

    }

    private void undoRedoNot(String msg) {
        undoRedo.setText(msg);
        undoRedo.setOpacity(1);
        FadeTransition fade = new FadeTransition(Duration.seconds(3), undoRedo);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setDelay(Duration.seconds(2));
        fade.play();
    }

    @FXML
    private void handleUndo() {
        if (undoStack.isEmpty()) return;
        UndoRedoManager lastAction = undoStack.pop();
        lastAction.undo();
        redoStack.push(lastAction);
        undoRedoNot(lastAction.description + " is un-done");

    }

    @FXML
    private void handleRedo() {
        if (redoStack.isEmpty()) return;
        UndoRedoManager lastAction = redoStack.pop();
        lastAction.redo();
        undoStack.push(lastAction);
        undoRedoNot(lastAction.description + " is re-done");
    }
}



