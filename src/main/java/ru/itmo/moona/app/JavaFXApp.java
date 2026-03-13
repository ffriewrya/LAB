package ru.itmo.moona.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.itmo.moona.gui.TableController;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.storage.FileStorage;
import ru.itmo.moona.storage.StockValidator;

public class JavaFXApp extends Application {
    private Stage primaryStage;
    StockManager manager = new StockManager();
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    FileStorage storage = new FileStorage(mapper, manager);
    StockValidator validator = new StockValidator();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../stockTable.fxml"));
        Scene scene = new Scene(loader.load());
        TableController controller = loader.getController();
        controller.setManager(manager);
        controller.setStorage(storage);
        controller.setValidator(validator);
        controller.setReagents(manager.getReagents());
        controller.setBatches(manager.getBatches());
        controller.setMoves(manager.getMoves());
        controller.initializeTable();
        primaryStage.setTitle("stock");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
