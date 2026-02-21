import ru.itmo.moona.Reagent;
import ru.itmo.moona.ReagentBatch;
import ru.itmo.moona.StockManager;

import java.time.Instant;

import static ru.itmo.moona.StockManager.*;

public class Main {
    public static void main(String[] args) {
        StockManager manager = new StockManager();



        try {
            Reagent newReagent = new Reagent.ReagentBuilder()
                    .setId(manager.genRId())
                    .setName("Sodium Chloride")
                    .setOwnerUsername("SYSTEM")
                    .setFormula("NaCl")
                    .setCas("120-319-3")
                    .setCreatedAt()
                    .setUpdatedAt()
                    .build();
            manager.addReagent(newReagent);



            Reagent newReagent1 = new Reagent.ReagentBuilder()
                    .setId(manager.genRId())
                    .setName("Sodium Chlorisddasdde")
                    .setOwnerUsername("SYSTEM")
                    .setFormula("NaCl")
                    .setCas("120-319-3")
                    .setHazardClass("low")
                    .setCreatedAt()
                    .setUpdatedAt()
                    .build();
            manager.addReagent(newReagent1);

            ReagentBatch batch1 = new ReagentBatch.BatchBuilder()
                    .setId(manager.genBId())
                    .setReagentId(1)
                    .setLabel("First Lab Batch")
                    .setQuantityCurrent(500.0)
                    .setUnit("G")
                    .setLocation("Shelf A1")
                    .setStatus("ACTIVE")
                    .setOwnerUsername("admin")
                    .setCreatedAt()
                    .setUpdatedAt()
                    .build();
            manager.addBatch(batch1);



            System.out.println(manager.getReagents());
            System.out.println(manager.getBatches());


        } catch (IllegalArgumentException e) {
            System.err.println("error >< " + e.getMessage());
        }




    }
}

