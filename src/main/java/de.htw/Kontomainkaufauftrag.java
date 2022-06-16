package de.htw;

public class Kontomainkaufauftrag implements Runnable{
    private double hoechstpreis;
    private int anzahl;
    private Aktie aktie;
    private Konto konto;

    public Kontomainkaufauftrag(Konto konto, Aktie aktie, int anzahl, double hoechstpreis) {
        this.konto = konto;
        this.aktie = aktie;
        this.anzahl = anzahl;
        this.hoechstpreis = hoechstpreis;
    }

    @Override
    public void run() {
        try {
            konto.kaufauftrag(aktie, anzahl, hoechstpreis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
