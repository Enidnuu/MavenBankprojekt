package de.htw;

import java.util.Random;

public class Aufgabe implements Runnable{

    private double result = 0;

    /**
     * Rueckgabe von einer random Prozentzahl
     * @return result
     */
    public double getResult() {
        return result;
    }
    public void setResult(double result) {
        this.result = result;
    }

    private double kurs = 0;

    /**
     * Rueckgabe des Wertes der Aktie
     * @return kurs
     */
    public double getKurs() { return kurs; }
    public void setKurs(double kurs) { this.kurs = kurs; }

    public Aufgabe(double kurs) {
        this.kurs = kurs;
    }

    @Override
    public void run() {
        int low = -300;
        int high = 300;
        Random random = new Random();
        setResult(((random.nextInt(high - low)+1)+low)/100.0);
        setKurs(getKurs()+(getKurs()*result)/100);
                                System.out.println(getKurs());
    }
}
