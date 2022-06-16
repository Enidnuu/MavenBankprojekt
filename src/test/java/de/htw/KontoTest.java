package de.htw;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static de.htw.Waehrung.*;
import static org.junit.jupiter.api.Assertions.*;

class KontoTest {

    Kunde kunde = new Kunde("Undine", "Wittner", "Ringelblumenweg", LocalDate.parse("2002-07-10"));
    Girokonto girokonto;
    Sparbuch sparbuch;


    @Test
    void einzahlenGirokontoTest() {
        girokonto = new Girokonto(kunde, 1234, 1000);
        girokonto.einzahlen(50.5, Waehrung.EUR);
        assertEquals(50.5, girokonto.getKontostand(), 0);
        girokonto.einzahlen(70, BGN);
        assertEquals(86.29, girokonto.getKontostand(), 0.1);
    }

    @Test
    void einzahlenGrenzwerteGirokontoTest() {
        girokonto = new Girokonto(kunde, 1234, 1000);
        girokonto.einzahlen(2500, Waehrung.EUR);
        assertEquals(2500, girokonto.getKontostand(), 0);
        girokonto.einzahlen(0, BGN);
        assertEquals(2500, girokonto.getKontostand(), 0);
        assertThrows(IllegalArgumentException.class, () -> {
            girokonto.einzahlen(-10, Waehrung.EUR);} );
    }

    @Test
    void abhebenGirokontoDispoTest() throws GesperrtException {
        girokonto = new Girokonto(kunde, 1234, 1000);
        assertTrue(girokonto.abheben(500));
        assertEquals(-500, girokonto.getKontostand(), 0);
        assertFalse(girokonto.abheben(600));
        assertEquals(-500, girokonto.getKontostand(), 0);
        assertTrue(girokonto.abheben(500));
        assertEquals(-1000, girokonto.getKontostand(), 0);
        assertFalse(girokonto.abheben(500));
        assertEquals(-1000, girokonto.getKontostand(), 0);
        assertThrows(IllegalArgumentException.class, () -> {
            girokonto.abheben(-500);} );
    }

    @Test
    void abhebenHintereinanderGirokontoTest() throws GesperrtException {
        girokonto = new Girokonto(kunde, 1234, 1000);
        assertTrue(girokonto.abheben(10));
        assertTrue(girokonto.abheben(10));
        assertEquals(-20, girokonto.getKontostand(), 0);
    }

    @Test
    void abhebenWennGesperrtGirokontoTest() throws GesperrtException {
        girokonto = new Girokonto(kunde, 1234, 1000);
        girokonto.sperren();
        assertThrows(GesperrtException.class, () -> {
            girokonto.abheben(10);});
    }

    @Test
    void waehrungswechselGirokontoTest() throws GesperrtException {
        girokonto = new Girokonto(kunde, 1234, 1000);
        girokonto.einzahlen(1000, Waehrung.EUR);
        girokonto.waehrungswechsel(DKK);
        assertEquals(7460.4, girokonto.getKontostand(), 0);
        assertEquals(7460.4, girokonto.getDispo(), 0);
        assertEquals(DKK, girokonto.getAktuelleWaehrung());
    }

    @Test
    void mehrfacherWaehrungswechselGirokontoTest() throws GesperrtException {
        girokonto = new Girokonto(kunde, 1234, 1000);
        girokonto.einzahlen(1000, Waehrung.EUR);
        girokonto.waehrungswechsel(DKK);
        assertEquals(7460.4, girokonto.getKontostand(), 0);
        assertEquals(7460.4, girokonto.getDispo(), 0);
        assertEquals(DKK, girokonto.getAktuelleWaehrung());
        girokonto.waehrungswechsel(BGN);
        assertEquals(1955.8, girokonto.getKontostand(), 0);
        assertEquals(1955.8, girokonto.getDispo(), 0);
        assertEquals(BGN, girokonto.getAktuelleWaehrung());
    }

    @Test
    void waehrungswechselEinzahlenAbhebenGirokontoTest() throws GesperrtException {
        girokonto = new Girokonto(kunde, 1234, 1000);
        girokonto.einzahlen(1000, EUR);
        girokonto.waehrungswechsel(DKK);
        girokonto.einzahlen(100, EUR);
        assertEquals(8206.44, girokonto.getKontostand(), 0.01);
        assertEquals(7460.4, girokonto.getDispo(), 0);
        assertEquals(DKK, girokonto.getAktuelleWaehrung());
        assertTrue(girokonto.abheben(100, BGN));
        assertEquals(7824.98, girokonto.getKontostand(), 0.01);
        assertEquals(7460.4, girokonto.getDispo(), 0);
        assertEquals(DKK, girokonto.getAktuelleWaehrung());
    }

    @Test
    void waehrungswechselSparbuchTest() throws GesperrtException {
        sparbuch = new Sparbuch();
        sparbuch.einzahlen(1100, Waehrung.EUR);
        assertTrue(sparbuch.abheben(100, EUR));
        sparbuch.waehrungswechsel(DKK);
        assertEquals(7460.4, sparbuch.getKontostand(), 0);
        assertEquals(746.04, sparbuch.getBereitsAbgehoben(), 0);
        assertEquals(DKK, sparbuch.getAktuelleWaehrung());
    }

    @Test
    void mehrfacherWaehrungswechselSparbuchTest() throws GesperrtException {
        sparbuch = new Sparbuch();
        sparbuch.einzahlen(1100, Waehrung.EUR);
        assertTrue(sparbuch.abheben(100, EUR));
        sparbuch.waehrungswechsel(DKK);
        assertEquals(7460.4, sparbuch.getKontostand(), 0);
        assertEquals(746.04, sparbuch.getBereitsAbgehoben(), 0);
        assertEquals(DKK, sparbuch.getAktuelleWaehrung());
        sparbuch.waehrungswechsel(BGN);
        assertEquals(1955.8, sparbuch.getKontostand(), 0);
        assertEquals(195.58, sparbuch.getBereitsAbgehoben(), 0.01);
        assertEquals(BGN, sparbuch.getAktuelleWaehrung());
    }

    @Test
    void waehrungswechselEinzahlenAbhebenSparbuchTest() throws GesperrtException {
        sparbuch = new Sparbuch();
        sparbuch.waehrungswechsel(DKK);
        sparbuch.einzahlen(1000, EUR);
        assertEquals(7460.4, sparbuch.getKontostand(), 0.01);
        assertEquals(0, sparbuch.getBereitsAbgehoben(), 0);
        assertEquals(DKK, sparbuch.getAktuelleWaehrung());
        assertTrue(sparbuch.abheben(100, BGN));
        assertEquals(7078.94, sparbuch.getKontostand(), 0.01);
        assertEquals(381.45, sparbuch.getBereitsAbgehoben(), 0.01);
        assertEquals(DKK, sparbuch.getAktuelleWaehrung());
    }

    @Test
    void einzahlenSparbuchTest() {
        sparbuch = new Sparbuch();
        sparbuch.einzahlen(30.5, Waehrung.EUR);
        assertEquals(30.5, sparbuch.getKontostand(), 0);
        sparbuch.einzahlen(60, DKK);
        assertEquals(38.54, sparbuch.getKontostand(), 0.1);
    }

    @Test
    void einzahlenGrenzwerteSparbuchTest() {
        sparbuch = new Sparbuch();
        sparbuch.einzahlen(5000, Waehrung.EUR);
        assertEquals(5000, sparbuch.getKontostand(), 0);
        sparbuch.einzahlen(0, MKD);
        assertEquals(5000, sparbuch.getKontostand(), 0);
        assertThrows(IllegalArgumentException.class, () -> {
            sparbuch.einzahlen(-20, Waehrung.EUR);
        });
    }

    @Test
    void abhebenHintereinanderSparbuchTest() throws GesperrtException{
        sparbuch = new Sparbuch();
        sparbuch.einzahlen(30.5, Waehrung.EUR);

        assertTrue(sparbuch.abheben(10, Waehrung.EUR));
        assertTrue(sparbuch.abheben(10, Waehrung.EUR));
        assertTrue(sparbuch.abheben(308.1, MKD));
        assertEquals(5.5, sparbuch.getKontostand(), 0.1);
    }

    @Test
    void abhebenLeeresSparbuchTest() throws GesperrtException{
        sparbuch = new Sparbuch();
        assertFalse(sparbuch.abheben(10, MKD));
        assertEquals(0, sparbuch.getKontostand(), 0);
    }

    @Test
    void abhebenWennGesperrtSparbuchTest() throws GesperrtException{
        sparbuch = new Sparbuch();
        sparbuch.sperren();
        assertThrows(GesperrtException.class, () -> {
            sparbuch.abheben(50, BGN);
        });
    }

}