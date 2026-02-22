package ru.itmo.moona.commands.base;

public interface Command {
    void execute(InputParser input);
    String getName();
    String getDescription();
}
