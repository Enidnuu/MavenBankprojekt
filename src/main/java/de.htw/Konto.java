package de.htw;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * stellt ein allgemeines Konto dar
 */
public abstract class Konto implements Comparable<Konto> {
    /**
     * der Kontoinhaber
     */
    private Kunde inhaber;

    /**
     * die Kontonummer
     */
    //private final entfernt
    long nummer;

    /**
     * Waehrung, in der das Konto gefuehrt wird
     */
    private Waehrung waehrung = Waehrung.EUR;

    /**
     * gibt die Waehrung des Kontos zurueck
     * @return waehrung
     */
    public Waehrung getAktuelleWaehrung() {
        return waehrung;
    }

    private void setAktuelleWaehrung(Waehrung waehrung){
        this.waehrung = waehrung;
    }

    /**
     * der aktuelle Kontostand
     */
    private double kontostand;

    /**
     * setzt den aktuellen Kontostand
     * @param kontostand neuer Kontostand
     */
    protected void setKontostand(double kontostand) {
        this.kontostand = kontostand;
    }

    /**
     * Wenn das Konto gesperrt ist (gesperrt = true), können keine Aktionen daran mehr vorgenommen werden,
     * die zum Schaden des Kontoinhabers wären (abheben, Inhaberwechsel)
     */
    private boolean gesperrt;

    /**
     * Setzt die beiden Eigenschaften kontoinhaber und kontonummer auf die angegebenen Werte,
     * der anfängliche Kontostand wird auf 0 gesetzt.
     *
     * @param inhaber der Inhaber
     * @param kontonummer die gewünschte Kontonummer
     * @throws IllegalArgumentException wenn der Inhaber null
     */
    public Konto(Kunde inhaber, long kontonummer) {
        if(inhaber == null)
            throw new IllegalArgumentException("Inhaber darf nicht null sein!");
        this.inhaber = inhaber;
        this.nummer = kontonummer;
        this.kontostand = 0;
        this.gesperrt = false;
    }

    /**
     * setzt alle Eigenschaften des Kontos auf Standardwerte
     */
    public Konto() {
        this(Kunde.MUSTERMANN, 1234567);
    }

    /**
     * liefert den Kontoinhaber zurück
     * @return   der Inhaber
     */
    public final Kunde getInhaber() {
        return this.inhaber;
    }

    /**
     * setzt den Kontoinhaber
     * @param kinh   neuer Kontoinhaber
     * @throws GesperrtException wenn das Konto gesperrt ist
     * @throws IllegalArgumentException wenn kinh null ist
     */
    public final void setInhaber(Kunde kinh) throws GesperrtException{
        if (kinh == null)
            throw new IllegalArgumentException("Der Inhaber darf nicht null sein!");
        if(this.gesperrt)
            throw new GesperrtException(this.nummer);
        this.inhaber = kinh;

    }

    /**
     * liefert den aktuellen Kontostand
     * @return   double
     */
    //final entfernt
    public double getKontostand() {
        return kontostand;
    }

    /**
     * liefert die Kontonummer zurück
     * @return   long
     */
    //final entfernt
    public long getKontonummer() {
        return nummer;
    }

    /**
     * liefert zurück, ob das Konto gesperrt ist oder nicht
     * @return true, wenn das Konto gesperrt ist
     */
    public final boolean isGesperrt() {
        return gesperrt;
    }

    /**
     * Erhöht den Kontostand um den eingezahlten Betrag.
     *
     * @param betrag double
     * @throws IllegalArgumentException wenn der betrag negativ ist
     */
    public void einzahlen(double betrag) {
        if (betrag < 0 || Double.isNaN(betrag)) {
            throw new IllegalArgumentException("Falscher Betrag");
        }
        setKontostand(getKontostand() + betrag);
    }

    /**
     * Gibt eine Zeichenkettendarstellung der Kontodaten zurück.
     */
    @Override
    public String toString() {
        String ausgabe;
        ausgabe = "Kontonummer: " + this.getKontonummerFormatiert()
                + System.getProperty("line.separator");
        ausgabe += "Inhaber: " + this.inhaber;
        ausgabe += "Aktueller Kontostand: " + getKontostandFormatiert() + " ";
        ausgabe += this.getGesperrtText() + System.getProperty("line.separator");
        return ausgabe;
    }

	/*
	public void ausgeben()
	{
		System.out.println(this.toString());
	}
	*/

    /**
     * Mit dieser Methode wird der geforderte Betrag vom Konto abgehoben, wenn es nicht gesperrt ist.
     *
     * @param betrag double
     * @throws GesperrtException wenn das Konto gesperrt ist
     * @throws IllegalArgumentException wenn der betrag negativ ist
     * @return true, wenn die Abhebung geklappt hat,
     * 		   false, wenn sie abgelehnt wurde
     */
    public abstract boolean abheben(double betrag)
            throws GesperrtException;

    /**
     * Mit dieser Methode wird der geforderte Betrag vom Konto abgehoben, wenn es nicht gesperrt ist.
     *
     * @param betrag double, w Waehrung
     * @throws GesperrtException wenn das Konto gesperrt ist
     * @throws IllegalArgumentException wenn der betrag negativ ist
     * @return true, wenn die Abhebung geklappt hat,
     * 		   false, wenn sie abgelehnt wurde
     */
    public boolean abheben(double betrag, Waehrung w) throws GesperrtException{
        double betragInKontoWaehrung = getAktuelleWaehrung().euroInWaehrungUmrechnen(w.waehrungInEuroUmrechnen(betrag)); // rechnet abzuhebende Währung in Euro um und dann Euro in Währung des Kontos.
        return abheben(betragInKontoWaehrung);
    }

    /**
     * Erhöht den Kontostand um den eingezahlten Betrag.
     *
     * @param betrag double
     * @throws IllegalArgumentException wenn der betrag negativ ist
     */
    public void einzahlen(double betrag, Waehrung w){
        double betragInKontoWaehrung = getAktuelleWaehrung().euroInWaehrungUmrechnen(w.waehrungInEuroUmrechnen(betrag));
        einzahlen(betragInKontoWaehrung);
    }

    /**
     *berechnet den Kontostand fuer die neue Waehrung und aendert die fuehrende Waehrung ab
     *
     * @param waehrung die neue fuehrende Waehrung
     */
    public void waehrungswechsel (Waehrung waehrung){
        double temp = getAktuelleWaehrung().waehrungInEuroUmrechnen(getKontostand());
        double temp2 = waehrung.euroInWaehrungUmrechnen(temp);
        setKontostand(temp2);
        setAktuelleWaehrung(waehrung);

    }


    /**
     * sperrt das Konto, Aktionen zum Schaden des Benutzers sind nicht mehr möglich.
     */
    public final void sperren() {
        this.gesperrt = true;
    }

    /**
     * entsperrt das Konto, alle Kontoaktionen sind wieder möglich.
     */
    public final void entsperren() {
        this.gesperrt = false;
    }


    /**
     * liefert eine String-Ausgabe, wenn das Konto gesperrt ist
     * @return "GESPERRT", wenn das Konto gesperrt ist, ansonsten ""
     */
    public final String getGesperrtText()
    {
        if (this.gesperrt)
        {
            return "GESPERRT";
        }
        else
        {
            return "";
        }
    }

    /**
     * liefert die ordentlich formatierte Kontonummer
     * @return auf 10 Stellen formatierte Kontonummer
     */
    public String getKontonummerFormatiert()
    {
        return String.format("%10d", this.nummer);
    }

    /**
     * liefert den ordentlich formatierten Kontostand
     * @return formatierter Kontostand mit 2 Nachkommastellen und Währungssymbol
     */
    public String getKontostandFormatiert() {
        return String.format("%10.2f " + getAktuelleWaehrung().toString() , this.getKontostand());
    }

    /**
     * Vergleich von this mit other; Zwei Konten gelten als gleich,
     * wen sie die gleiche Kontonummer haben
     * @param other das Vergleichskonto
     * @return true, wenn beide Konten die gleiche Nummer haben
     */
    @Override
    public boolean equals(Object other)
    {
        if(this == other)
            return true;
        if(other == null)
            return false;
        if(this.getClass() != other.getClass())
            return false;
        if(this.nummer == ((Konto)other).nummer)
            return true;
        else
            return false;
    }

    /**
     * Attribut zur Speicherung des Aktiendepots, Name und Anzahl der Aktie
     */
    private Map<Aktie, Integer> aktiendepot = new HashMap<>();      //Aktie aktie, Integer Stueckzahl

    /**
     * Methode zum Kaufen von Aktien, sobald sie unter dem Hoechstpreis liegen
     * @param a Aktie
     * @param anzahl
     * @param hoechstpreis
     * @return executorService.submit(callable) , der bezahlte Preis
     * @throws Exception
     */
    public Future<Double> kaufauftrag(Aktie a, int anzahl, double hoechstpreis) throws Exception {
        Callable<Double> callable = () -> {
            double gesamtpreis = 0;
            while (a.getKurs() > hoechstpreis) { }
            if (getKontostand() < anzahl * a.getKurs()) {
                return gesamtpreis;
            }
            gesamtpreis = a.getKurs() * anzahl;
            abheben(gesamtpreis);
            aktiendepot.put(a, anzahl);
                                    System.out.println(aktiendepot);
            return gesamtpreis;
        };
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        return executorService.submit(callable);
    }

    /**
     * Methode zum Verkaufen von einer Aktie, sobald sie ueber dem Minimalpreis liegt
     * @param wkn wertkennnummer
     * @param minimalpreis
     * @return executorService.submit(callable) , der angerechnete Preis
     */
    public Future<Double> verkaufauftrag(String wkn, double minimalpreis) {
        Callable<Double> callable = () -> {
            double gesamtpreis = 0;
            for (Map.Entry<Aktie, Integer> a :aktiendepot.entrySet()) {
                Aktie aktie = a.getKey();
                if (aktie.getWertpapierkennnummer() == wkn) {
                        while (aktie.getKurs() < minimalpreis) { }
                        gesamtpreis = aktie.getKurs() * a.getValue();
                        einzahlen(gesamtpreis);
                        aktiendepot.remove(a.getKey(), a.getValue());
                        return gesamtpreis;
                }
            }
            return gesamtpreis;
        };
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        return executorService.submit(callable);
    }


    @Override
    public int hashCode()
    {
        return 31 + (int) (this.nummer ^ (this.nummer >>> 32));
    }

    @Override
    public int compareTo(Konto other)
    {
        if(other.getKontonummer() > this.getKontonummer())
            return -1;
        if(other.getKontonummer() < this.getKontonummer())
            return 1;
        return 0;
    }
}