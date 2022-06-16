package de.htw;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * stellt eine allgemeine Bank da.
 * @author Undine Wittner
 */
public class Bank {

    /**
     * Die Bankleitzahl einer Bank (deren Schlüssel)
     */
    private long bankleitzahl;

    private long counter = 0;

    public Map<Long, Konto> getKontenliste() {
        return kontenliste;
    }

    /**
     * Map, auf der alle Konten der Bank gespeichert werden
     */
    Map<Long, Konto> kontenliste = new HashMap<>();

    public Bank(long bankleitzahl) {
        this.bankleitzahl = bankleitzahl;
    }

    public long getBankleitzahl() {
        return bankleitzahl;
    }

    /**
     * Methode zur Erstellung einer randomisierten long Nummer (zur Verwendung für Kontonummer)
     * @return kontonummer, die random. Zahl
     */
    public long erstelleKontonummer() {
        counter++;
        long kontonummer = counter;
        return kontonummer;
    }

    /**
     * Methode zur Erstellung eines Girokontos mit einer randomisierten Nummer
     * @param inhaber wird zur Erstellung eines Kontos benoetigt
     * @return kontonummer des Girokontos
     */
    public long girokontoErstellen(Kunde inhaber) {

        long kontonummer = erstelleKontonummer();

        Girokonto girokonto = new Girokonto(inhaber, kontonummer, 1000);
        kontenliste.put(kontonummer, girokonto);
        return kontonummer;
    }

    /**
     * Methode, um Mockobjekte verwenden zu koennen, um Bank zu testen
     * @param k das zu uebergebende MockKonto
     * @return kontonummer
     */
    public long mockEinfuegen(Konto k) {

        long kontonummer = erstelleKontonummer();
        kontenliste.put(kontonummer, k);
        return kontonummer;
    }

    /**
     * Methode zur Erstellung eines Girokontos mit einer randomisierten Nummer
     * @param inhaber wird zur Erstellung eines Kontos benoetigt
     * @return kontonummer des Sparbuchs
     */
    public long sparbuchErstellen(Kunde inhaber) {

        long kontonummer = erstelleKontonummer();

        Sparbuch sparbuch = new Sparbuch(inhaber, kontonummer);
        kontenliste.put(kontonummer, sparbuch);
        return kontonummer;
    }

    /**
     * Alle Konten, die in der kontenliste gespeichert sind, werden hier anhand ihrer Kontonummer und Kontostand in einen String geschrieben
     * @return string, aus all den Kontoinformationen
     */
    public String getAlleKonten() {
        StringBuilder stringbuilder = new StringBuilder(kontenliste.size());
        stringbuilder.append("Kontoinformationen: " + System.lineSeparator());
        for (Long key : kontenliste.keySet()) {
            stringbuilder.append("Kontonummer: " + key + " , ");
            Konto value = kontenliste.get(key);
            stringbuilder.append("Kontostand: " + value.getKontostand() + System.lineSeparator());
        }
        String string = new String(stringbuilder);
        return string;
    }

    /**
     * Die Kontonummern werden in die Liste kontonummernListe geschrieben
     * @return kontonummernListe
     */
    public List<Long> getAlleKontonummern() {
        List<Long> kontonummernListe = new LinkedList<>(kontenliste.keySet());
        return kontonummernListe;
    }

    /**
     * Methode, fuers Geld abheben
     * @param von die kontonummer (das Konto), von dem abgehoben wird
     * @param betrag , der abgehoben werden soll
     * @return die aufgerufene Methode, weil die anhand des Erfolgs der Methode einen passenden boolean returnt
     * @throws GesperrtException , wenn ein Konto gesperrt ist
     */
    public boolean geldAbheben(long von, double betrag) throws GesperrtException {
        return kontenliste.get(von).abheben(betrag);
    }

    /**
     * Methode, fuers Geld einzahlen
     * @param auf die kontonummer (das Konto), auf die eingezahlt werden soll
     * @param betrag , der eingezahlt werden soll
     * @return die aufgerufene Methode, weil die anhand des Erfolgs der Methode einen passenden boolean returnt
     */
    public void geldEinzahlen(long auf, double betrag) {
        kontenliste.get(auf).einzahlen(betrag);
    }

    /**
     * Loescht ein Konto von der kontenliste
     * @param nummer , deren Konto geloescht werden soll
     * @return true , wenn erfolgreich
     * @return false , wenn nicht erfolgreich
     */
    public boolean kontoLoeschen(long nummer) {
        if (kontenliste.containsKey(nummer)) {
            kontenliste.remove(nummer);
            return true;
        }
        return false;
    }

    /**
     * Methode, um kontostand anhand der kontonummer zu erhalten
     * @param nummer die kontonummer
     * @return die aufgerufene Methode, weil sie den kontostand returnt
     */
    public double getKontostand(long nummer) {
        return kontenliste.get(nummer).getKontostand();
    }

    /**
     * Methode, die Ueberweisungen ermoeglicht
     * @param vonKontonr von welchem Konto die Ueberweisung ausgeht
     * @param nachKontonr auf welches Konto ueberwiesen wird
     * @param betrag welcher Betrag ueberwiesen werden soll
     * @param verwendungszweck warum ueberwiesen wird
     * @return true falls das Absenden vom Geld moeglich ist
     * @throws KeinUeberweisungsfaehigesKontoException falls min. eines der beiden Konten nicht Ueberweisungsfaehig ist
     * @throws GleichesKontoException falls die Konten, zwischen denen Geld ausgetauscht werden soll, das gleiche Konto sind
     * @throws GesperrtException falls das Konto, dass ueberweisen soll gesperrt ist
     */
    public boolean geldUeberweisen(long vonKontonr, long nachKontonr, double betrag, String verwendungszweck) throws KeinUeberweisungsfaehigesKontoException, GleichesKontoException, GesperrtException {
        if ((kontenliste.get(vonKontonr) instanceof Ueberweisungsfaehig || kontenliste.get(nachKontonr) instanceof Ueberweisungsfaehig) == false) {
            throw new KeinUeberweisungsfaehigesKontoException("Eines der Konten ist nicht ueberweisungsfaehig.");
        }
        if (kontenliste.get(vonKontonr) == kontenliste.get(nachKontonr)) {
            throw new GleichesKontoException("Die Kontonummern beziehen sich auf das gleiche Konto.");
        }
        if (((Ueberweisungsfaehig) kontenliste.get(vonKontonr)).ueberweisungAbsenden(betrag, kontenliste.get(nachKontonr).getInhaber().getName(), nachKontonr, this.bankleitzahl, verwendungszweck) == true);
            ((Ueberweisungsfaehig) kontenliste.get(nachKontonr)).ueberweisungEmpfangen(betrag, kontenliste.get(vonKontonr).getInhaber().getName(), vonKontonr, this.bankleitzahl, verwendungszweck);
            return true;
    }

    /**
     * Methode, die alles mit einem Kontostand kleiner 0 sperrt
     */
    public void pleitegeierSperren() {
        kontenliste.values()
                .stream()
                .filter(konto -> konto.getKontostand() < 0)
                .forEach(Konto::sperren);
    }

    /**
     * Methode, die alle Kunden ausgibt, die auf mindestens einem Konto den uebergebenen Minimal-Wert an Geld besitzen
     * @param minimum Wert, der den Mindestkontostand angibt
     * @return Liste, die die Kunden enthaelt
     */
    public List<Kunde> getKundenMitVollemKonto(double minimum) {
        Set set = new HashSet();
        kontenliste.values()
                .stream()
                .filter(konto -> konto.getKontostand() > minimum)
                .forEach(konto -> set.add(konto.getInhaber()));
        return new LinkedList<>(set);
    }

    /**
     * Returnt einen String, in dem die Namen und Geburtstage alle Kunden ausgegeben werden, nach Geburtstag sortiert
     * @return String, der die Namen und Geburtstage formatiert uebergibt
     */
    public String getKundengeburtstage() {
        Comparator<Kunde> birthdayComparator = Comparator.comparing(Kunde::getGeburtstag);
        TreeSet<Kunde> set;
        set = kontenliste.values()
                .stream()
                .map(Konto::getInhaber)
                .collect(
                Collectors.toCollection(
                        () -> new TreeSet<>(birthdayComparator)));
        return set.stream().map(kunde -> kunde.getName() + ", " + kunde.getGeburtstag()).collect(Collectors.joining(" | "));
        }

    /**
     * Gibt eine Liste aus Kontonummern zurueck, welche zwischen anderen Kontonummern stehen und nicht vergeben sind
      * @return Liste aus den Kontonummern
     */
    public List<Long> getKontonummernLuecken() {
        return LongStream.rangeClosed(0, counter)
                .filter(i ->! kontenliste.containsKey(i))
                .boxed()
                .collect(Collectors.toList());
    }

}
