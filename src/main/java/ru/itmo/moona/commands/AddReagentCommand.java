package ru.itmo.moona.commands;

import ru.itmo.moona.Reagent;
import ru.itmo.moona.StockManager;
import ru.itmo.moona.commands.base.Command;
import ru.itmo.moona.commands.base.InputParser;
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
            System.out.println("enter reagent name");
            String name = scanner.nextLine();
            builder.setName(name);
            break;
        }

        while (true) {
            System.out.println("enter reagent formula");
            String formula = scanner.nextLine();
            builder.setFormula(formula);
            break;
        }

        while (true) {
            System.out.println("enter CAS");
            String cas = scanner.nextLine();
            builder.setCas(cas);
            break;
        }

        while (true) {
            System.out.println("enter hazard class");
            String hazard = scanner.nextLine();
            builder.setHazardClass(hazard);
            break;
        }

        Reagent reagent = builder.build();
        StockManager.addReagent(reagent);
        System.out.println("successfully added a reagent " + reagent.getId());
        //todo исключения


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
