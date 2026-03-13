package ru.itmo.moona;


import javafx.application.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.itmo.moona.gui.TableController;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.storage.FileStorage;
import ru.itmo.moona.storage.StockValidator;

public class Main extends Application {
    private Stage primaryStage;
    private AnchorPane rootLayout;
    StockManager manager = new StockManager();
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    FileStorage storage = new FileStorage(mapper, manager);
    StockValidator validator = new StockValidator();

    @Override
    public void start(Stage primaryStage) throws Exception {
        StockValidator validator = new StockValidator();
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("stockTable.fxml"));
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


    public static void main(String[] args) {
//        CommandManager commandManager = new CommandManager();
//        StockManager manager = new StockManager();
//        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
//        FileStorage storage = new FileStorage(mapper, manager);
//        StockValidator validator = new StockValidator();
//        Scanner scanner = new Scanner(System.in);
//        commandManager.create(new HelpCommand(commandManager.getCommands()));
//        commandManager.create(new AddBatchCommand(scanner, manager));
//        commandManager.create(new AddMoveCommand(scanner, manager));
//        commandManager.create(new AddReagentCommand(scanner, manager));
//        commandManager.create(new BatchArchiveCommand(manager));
//        commandManager.create(new BatchListCommand(manager));
//        commandManager.create(new BatchShowCommand(manager));
//        commandManager.create(new MoveListCommand(manager));
//        commandManager.create(new ReagentListCommand(manager));
//        commandManager.create(new StockReportCommand(manager));
//        commandManager.create(new UpdateBatchCommand(manager));
//        commandManager.create(new ExitCommand());
//        commandManager.create(new UndoCommand(commandManager));
//        commandManager.create(new RedoCommand(commandManager));
//        commandManager.create(new BatchHistoryCommand(manager));
//        commandManager.create(new SaveCommand(manager, storage));
//        commandManager.create(new LoadCommand(manager, storage, validator));
//
//        System.out.println("hiiiiIIIiiiiii! welcome to Reagent Stock");
//        System.out.println("type 'help' for a list of commands");
//
//        while (scanner.hasNextLine()) {
//            String input = scanner.nextLine().trim();
//            if (input.isBlank()) {
//                continue;
//            }
//            try {
//                commandManager.start(input);
//            } catch (IllegalArgumentException e) {
//                System.err.println("error! " + e.getMessage());
//            } catch (Exception e) {
//                System.err.println("an unexpected error " + e.getMessage());
//            }
//        }

        Application.launch();
    }
}



