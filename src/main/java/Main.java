import ru.itmo.moona.Reagent;
import ru.itmo.moona.ReagentBatch;
import ru.itmo.moona.StockManager;
import ru.itmo.moona.StockMove;

import static ru.itmo.moona.StockManager.*;

public class Main {
    public static void main(String[] args) {
        StockManager manager = new StockManager();


        try {
            Reagent newReagent = new Reagent.ReagentBuilder()
                    .setId(manager.genReagentId())
                    .setName("Sodium Chloride")
                    .setOwnerUsername("SYSTEM")
                    .setFormula("NaCl")
                    .setCas("120-319-3")
                    .setHazardClass("")
                    .setCreatedAt()
                    .setUpdatedAt()
                    .build();
            manager.addReagent(newReagent);


            Reagent newReagent1 = new Reagent.ReagentBuilder()
                    .setId(manager.genReagentId())
                    .setName("Sulphine Sodium")
                    .setOwnerUsername("SYSTEM")
                    .setFormula("NaCl")
                    .setCas("120-319-3")
                    .setHazardClass("low")
                    .setCreatedAt()
                    .setUpdatedAt()
                    .build();
            manager.addReagent(newReagent1);

            ReagentBatch batch1 = new ReagentBatch.BatchBuilder()
                    .setId(manager.genBatchId())
                    .setReagentId(1)
                    .setLabel("First Batch")
                    .setQuantityCurrent(500.0)
                    .setUnit("G")
                    .setLocation("Shelf A1")
                    .setStatus("ARCHIVED")
                    .setOwnerUsername("SYSTEM")
                    .setExpiresAt("01-06-2026")
                    .setCreatedAt()
                    .setUpdatedAt()
                    .build();
            manager.addBatch(batch1);


            StockMove move = new StockMove.MoveBuilder()
                    .setId(manager.genMoveId())
                    .setBatchId(1)
                    .setType("in")
                    .setQuantity(500)
                    .setUnit()
                    .setReason("just did")
                    .setOwnerUsername("SYSTEM")
                    .setMovedAt("02-06-2026")
                    .setCreatedAt()
                    .build();
            manager.addMove(move);

            printReagents();
            System.out.println();
            printBatches();
            System.out.println();
            printMoves();
            System.out.println();
            findReagent("sodium");
            System.out.println();
            findBatch(1);
            System.out.println();
            showBatch(1);
            stockReport();


        } catch (IllegalArgumentException e) {
            System.err.println("error! " + e.getMessage());
        }

        /*
         todo try catch для парсера
         todo побольше исключений для команд
         todo проверить по тз
         todo подписать что optional
        */


    }
}

