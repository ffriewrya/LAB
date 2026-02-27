package ru.itmo.moona.cli.base;

public interface Command {
    void execute(InputParser input);

    String getName();

    String getDescription();
}
