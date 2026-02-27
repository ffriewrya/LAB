import ru.itmo.moona.cli.base.CommandManager;
import ru.itmo.moona.cli.commands.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CommandManager commandManager = new CommandManager();
        Scanner scanner = new Scanner(System.in);
        commandManager.create(new HelpCommand(commandManager.getCommands()));
        commandManager.create(new AddBatchCommand(scanner));
        commandManager.create(new AddMoveCommand(scanner));
        commandManager.create(new AddReagentCommand(scanner));
        commandManager.create(new BatchArchiveCommand());
        commandManager.create(new BatchListCommand());
        commandManager.create(new BatchShowCommand());
        commandManager.create(new MoveListCommand());
        commandManager.create(new ReagentListCommand());
        commandManager.create(new StockReportCommand());
        commandManager.create(new UpdateBatchCommand());
        commandManager.create(new ExitCommand());

        System.out.println("hiiiiIIIiiiiii! welcome to Reagent Stock");
        System.out.println("type 'help' for a list of commands");

        while (true) {
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
}

