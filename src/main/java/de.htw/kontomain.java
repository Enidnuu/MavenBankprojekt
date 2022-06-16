package de.htw;


import java.time.LocalDate;
import java.util.concurrent.*;

public class kontomain {

    public static void main (String []args) throws Exception, GleichesKontoException {
        Kunde kunde = new Kunde("Max", "Muster", "Adresse 2", LocalDate.parse("1999-02-13"));
        Konto nummer0 = new Girokonto(kunde, 123, 0);

        nummer0.einzahlen(2000);

        Aktie tesla = new Aktie("Tesla", 10.0, "T123");
        Aktie mercedes = new Aktie("Mercedes Benz", 20, "M234");
        Aktie bauer = new Aktie("Bauer", 300, "B666");

        Kontomainkaufauftrag kontomainkaufauftrag = new Kontomainkaufauftrag(nummer0, tesla, 3, 9.0);
        Kontomainkaufauftrag kontomainkaufauftrag2 = new Kontomainkaufauftrag(nummer0, bauer, 1, 298.0);
        Kontomainkaufauftrag kontomainkaufauftrag3 = new Kontomainkaufauftrag(nummer0, mercedes, 2, 18.0);
        Kontomainverkaufauftrag kontomainverkaufauftrag = new Kontomainverkaufauftrag(nummer0, "T123", 14.0);
        Kontomainverkaufauftrag kontomainverkaufauftrag2 = new Kontomainverkaufauftrag(nummer0, "M234", 22.0);
        Kontomainverkaufauftrag kontomainverkaufauftrag3 = new Kontomainverkaufauftrag(nummer0, "B666", 320.0);

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(6);
        System.out.println(scheduledExecutorService.scheduleAtFixedRate(kontomainkaufauftrag, 1, 1, TimeUnit.SECONDS).get());
        scheduledExecutorService.scheduleAtFixedRate(kontomainkaufauftrag2, 1, 1, TimeUnit.SECONDS).get();
        scheduledExecutorService.scheduleAtFixedRate(kontomainkaufauftrag3, 1, 1, TimeUnit.SECONDS).get();
        scheduledExecutorService.scheduleAtFixedRate(kontomainverkaufauftrag, 1, 1, TimeUnit.SECONDS).get();
        scheduledExecutorService.scheduleAtFixedRate(kontomainverkaufauftrag2, 1, 1, TimeUnit.SECONDS).get();
        scheduledExecutorService.scheduleAtFixedRate(kontomainverkaufauftrag3, 1, 1, TimeUnit.SECONDS).get();

        scheduledExecutorService.shutdown();
    }
}
