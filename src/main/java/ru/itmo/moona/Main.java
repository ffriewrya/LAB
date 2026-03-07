package ru.itmo.moona;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itmo.moona.cli.base.CommandManager;
import ru.itmo.moona.cli.commands.*;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.storage.FileStorage;
import ru.itmo.moona.storage.StockValidator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CommandManager commandManager = new CommandManager();
        StockManager manager = new StockManager();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        FileStorage storage = new FileStorage(mapper, manager);
        StockValidator validator = new StockValidator();
        Scanner scanner = new Scanner(System.in);
        commandManager.create(new HelpCommand(commandManager.getCommands()));
        commandManager.create(new AddBatchCommand(scanner, manager));
        commandManager.create(new AddMoveCommand(scanner, manager));
        commandManager.create(new AddReagentCommand(scanner, manager));
        commandManager.create(new BatchArchiveCommand(manager));
        commandManager.create(new BatchListCommand(manager));
        commandManager.create(new BatchShowCommand(manager));
        commandManager.create(new MoveListCommand(manager));
        commandManager.create(new ReagentListCommand(manager));
        commandManager.create(new StockReportCommand(manager));
        commandManager.create(new UpdateBatchCommand(manager));
        commandManager.create(new ExitCommand());
        commandManager.create(new UndoCommand(commandManager));
        commandManager.create(new RedoCommand(commandManager));
        commandManager.create(new BatchHistoryCommand(manager));
        commandManager.create(new SaveCommand(manager, storage));
        commandManager.create(new LoadCommand(manager, storage, validator));

        System.out.println("hiiiiIIIiiiiii! welcome to Reagent Stock");
        System.out.println("type 'help' for a list of commands");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isBlank()) {
                continue;
            }
            try {
                commandManager.start(input);
            } catch (IllegalArgumentException e) {
                System.err.println("error! " + e.getMessage());
            } catch (Exception e) {
                System.err.println("an unexpected error " + e.getMessage());
            }
        }
    }

    //todo jsonautodetect
    //todo undo redo для сейва и вообще про undo подумать
    //todo для истории прчоекать команды
    //todo ignore unknown
}

