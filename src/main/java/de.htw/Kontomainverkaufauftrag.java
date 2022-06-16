package de.htw;

public class Kontomainverkaufauftrag implements Runnable{
    private Konto konto;
    private String wertkennnummer;
    private double minimalpreis;

    public Kontomainverkaufauftrag(Konto konto, String wertkennnummer, double minimalpreis) {
        this.wertkennnummer = wertkennnummer;
        this.minimalpreis = minimalpreis;
        this.konto = konto;
    }

    @Override
    public void run() {
        konto.verkaufauftrag(wertkennnummer, minimalpreis);
    }
}
