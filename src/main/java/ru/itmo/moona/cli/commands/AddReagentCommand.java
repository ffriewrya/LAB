package ru.itmo.moona.cli.commands;

import ru.itmo.moona.domain.Reagent;
import ru.itmo.moona.service.StockManager;
import ru.itmo.moona.cli.base.Command;
import ru.itmo.moona.cli.base.InputParser;

import java.util.Scanner;

public class AddReagentCommand implements Command {

    private final Scanner scanner;

    public AddReagentCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(InputParser input) {
        Reagent.ReagentBuilder builder = new Reagent.ReagentBuilder();
        builder.setId(StockManager.genReagentId());
        builder.setCreatedAt();
        builder.setUpdatedAt();
        builder.setOwnerUsername("SYSTEM");

        while (true) {
            try {
                System.out.println("enter reagent name");
                String name = scanner.nextLine();
                builder.setName(name);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
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

        Reagent reagent = builder.build();
        StockManager.addReagent(reagent);
        System.out.println("successfully added a reagent " + reagent.getId());


    }

    @Override
    public String getName() {
        return "reag_add";
    }

    @Override
    public String getDescription() {
        return "creates a reagent";
    }
}
