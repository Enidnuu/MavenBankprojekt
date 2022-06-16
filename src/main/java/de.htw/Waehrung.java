package de.htw;

public enum Waehrung {
    EUR (1),
    BGN (1.9558),
    DKK (7.4604),
    MKD (61.62);

    private double kurs;

    private Waehrung(double kurs){
        this.kurs = kurs;
    }

    /**
     * Getter, um den Kurs einzusehen
     *
     * @return den derzeitigen Kurs
     */
    public double getKurs(){
        return this.kurs;
    }

    /**
     * soll den in Euro angegebenen Betrag in die jeweilige Waehrung umrechnen
     *
     * @param betrag welcher umgerechnet werden soll
     * @return umgerechneter Betrag
     */
    public double euroInWaehrungUmrechnen(double betrag){
        return betrag*kurs;
    }

    /**
     * soll den in einer beliebigen Waehrung erhaltenen Betrag in Euro umrechnen
     *
     * @param betrag welcher umgerechnet werden soll
     * @return umgerechneter Betrag
     */
    public double waehrungInEuroUmrechnen(double betrag){
        return betrag/kurs;
    }
}
