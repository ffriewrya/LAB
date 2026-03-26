package ru.itmo.moona.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.itmo.moona.database.*;
import ru.itmo.moona.domain.users.User;
import ru.itmo.moona.gui.LoginController;
import ru.itmo.moona.gui.TableController;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.storage.FileStorage;
import ru.itmo.moona.storage.StockValidator;

import java.io.IOException;
import java.sql.SQLException;

public class JavaFXApp extends Application {
    private Stage primaryStage;
    StockManager manager = new StockManager();
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    FileStorage storage = new FileStorage(mapper, manager);
    StockValidator validator = new StockValidator();
    DatabaseManager databaseManager = new DatabaseManager();
    UserRepository userRepo = new UserRepository(databaseManager);
    ReagentRepository reagRepo = new ReagentRepository(databaseManager);
    BatchRepository batchRepo = new BatchRepository(databaseManager, manager);
    MoveRepository moveRepo = new MoveRepository(databaseManager);

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../loginWindow.fxml"));
        Scene scene = new Scene(loader.load());

        LoginController controller = loader.getController();

        controller.setApp(this);
        controller.setRepository(userRepo);

        primaryStage.setTitle("login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showTable() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../stockTable.fxml"));
        Scene scene = new Scene(loader.load());
        TableController controller = loader.getController();
        controller.setManager(manager);
        controller.setStorage(storage);
        controller.setValidator(validator);
        controller.setReagents(manager.getReagents());
        controller.setReagentRepository(reagRepo);
        controller.setBatchRepository(batchRepo);
        controller.setMoveRepository(moveRepo);
        manager.setReagents(reagRepo.getReagents());
        manager.setBatches(batchRepo.getBatches());
        manager.setMoves(moveRepo.getMoves());
        controller.setBatches(manager.getBatches());
        controller.setMoves(manager.getMoves());
        controller.initializeTable();
        primaryStage.setTitle("stock");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
