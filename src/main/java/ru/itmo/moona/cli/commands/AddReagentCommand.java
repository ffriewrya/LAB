package ru.itmo.moona.cli.commands;

import ru.itmo.moona.cli.base.Undoable;
import ru.itmo.moona.domain.Reagent;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;

import java.util.Scanner;

import static ru.itmo.moona.service.StockManager.*;

public class AddReagentCommand implements Command, Undoable {

    private final Scanner scanner;
    private Reagent createdReagent;

    public AddReagentCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(InputParser input) {

        Reagent.ReagentBuilder builder = new Reagent.ReagentBuilder();
        builder.setId(genReagentId());
        builder.setCreatedAt();
        builder.setUpdatedAt();
        builder.setOwnerUsername("SYSTEM");

        try {
            System.out.println("enter reagent name");
            String name = scanner.nextLine();
            builder.setName(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }


        while (true) {
            try {
                System.out.println("enter reagent formula (optional)");
                String formula = scanner.nextLine();
                builder.setFormula(formula);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        while (true) {
            try {
                System.out.println("enter CAS (optional)");
                String cas = scanner.nextLine();
                builder.setCas(cas);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        while (true) {
            try {
                System.out.println("enter hazard class (optional)");
                String hazard = scanner.nextLine();
                builder.setHazardClass(hazard);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

        createdReagent = builder.build();
        addReagent(createdReagent);
        System.out.println("successfully added a reagent " + createdReagent.getId());


    }

    @Override
    public String getName() {
        return "reag_add";
    }

    @Override
    public String getDescription() {
        return "creates a reagent";
    }

    @Override
    public void undo() {
        if (createdReagent == null) {
            throw new IllegalArgumentException("there is no created reagent. can't undo.");
        }
        removeReagent(createdReagent);
        System.out.println("successfully removed reagent " + createdReagent.getId());
    }

    @Override
    public void redo() {
        if (createdReagent == null) {
            throw new IllegalArgumentException("there is no created reagent. can't redo.");
        }
        if (!isReagentExists(createdReagent.getId())) {
            StockManager.redoReagent(createdReagent);
            System.out.println("reagent " + createdReagent.getId() + " has been re-added.");
        } else {
            throw new IllegalArgumentException("reagent already exists. can't redo.");
        }
    }
}
