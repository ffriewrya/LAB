package ru.itmo.moona.cli.base;

public interface Undoable {
    void undo();

    void redo();
}
