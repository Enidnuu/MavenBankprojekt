package de.htw;

import java.util.concurrent.*;

public class Aktie {

    private long counter = 0;
    private String name;
    private String wertpapierkennnummer;
    public String getWertpapierkennnummer() {
        return this.wertpapierkennnummer;
    }
    private double kurs;
    public double getKurs(){
        return kurs;
    }
    public void setKurs(double kurs){
        this.kurs = kurs;
    }

    /**
     * Konstruktor einer Aktie, der Wert veraendert sich jede Sekunde
     * @param name
     * @param kurs , Wert der Aktie
     */
    public Aktie(String name, double kurs, String wertpapierkennnummer) {
        this.kurs = kurs;
        this.name = name;
        this.wertpapierkennnummer = wertpapierkennnummer;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Aufgabe aufgabe = new Aufgabe(getKurs());
        scheduledExecutorService.scheduleAtFixedRate(aufgabe, 1, 1, TimeUnit.SECONDS);
        setKurs(aufgabe.getKurs());
    }
}
