import ru.itmo.moona.Reagent;
import ru.itmo.moona.ReagentBatch;
import ru.itmo.moona.StockManager;

import java.time.Instant;

import static ru.itmo.moona.StockManager.*;

public class Main {
    public static void main(String[] args) {
        StockManager manager = new StockManager();


        //переделать вотд класс менеджер


        try {
            Reagent newReagent = new Reagent.ReagentBuilder()
                    .setId(manager.genRId())
                    .setName("Sodium Chloride")
                    .setOwnerUsername("SYSTEM")
                    .setFormula("NaCl")
                    .setCas("120-319-3")
                    .setCreatedAt(Instant.now())
                    .setUpdatedAt(Instant.now())
                    .build();
            manager.addReagent(newReagent);


            // подумать насчет add


            Reagent newReagent1 = new Reagent.ReagentBuilder()
                    .setId(manager.genRId())
                    .setName("Sodium Chlorisddasdde")
                    .setOwnerUsername("SYSTEM")
                    .setFormula("NaCl")
                    .setCas("120-319-3")
                    .setHazardClass("low")
                    .setCreatedAt(Instant.now())
                    .setUpdatedAt(Instant.now())
                    .build();
            manager.addReagent(newReagent1);

            ReagentBatch batch1 = new ReagentBatch.BatchBuilder()
                    .setId(manager.genBId())
                    .setReagentId(1L, manager.getReagents())
                    .setLabel("First Lab Batch")
                    .setQuantityCurrent(500.0)
                    .setUnit("G")
                    .setLocation("Shelf A1")
                    .setStatus("ACTIVE")
                    .setOwnerUsername("admin")
                    .setCreatedAt(Instant.now())
                    .setUpdatedAt(Instant.now())
                    .build();
            manager.addBatch(batch1);

        } catch (IllegalArgumentException e) {
            System.err.println("error >< " + e.getMessage());
        }

        System.out.println(manager.getReagents());
        System.out.println(manager.getBatches());


    }
}

