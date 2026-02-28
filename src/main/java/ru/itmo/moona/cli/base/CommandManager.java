package ru.itmo.moona.cli.base;

import java.util.HashMap;
import java.util.Stack;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final static Stack<Undoable> undoStack = new Stack<>();
    private final static Stack<Undoable> redoStack = new Stack<>();

    public void create(Command command) {
        commands.put(command.getName(), command);
    }

    public void start(String rawInput) {
        InputParser input = new InputParser(rawInput);
        Command cmd = commands.get(input.getCommandName());

        if (cmd == null) {
            throw new IllegalArgumentException("this command doesn't exist");
        } else {
            cmd.execute(input);
            if (cmd instanceof Undoable) {
                enroll((Undoable) cmd);
            }
        }
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public void enroll(Undoable cmd) {
        undoStack.push(cmd);
        redoStack.clear();
    }

    public static void undo() {
        if (undoStack.isEmpty()) {
            throw new IllegalArgumentException("nothing to undo.");
        }
        Undoable cmd = undoStack.pop();
        cmd.undo();
        redoStack.push(cmd);
    }

    public static void redo() {
        if (redoStack.isEmpty()) {
            throw new IllegalArgumentException("nothing to redo");
        }
        Undoable cmd = redoStack.pop();
        cmd.redo();
        undoStack.push(cmd);
    }
}
