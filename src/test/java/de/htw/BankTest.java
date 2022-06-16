package de.htw;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;

class BankTest {

    Bank bank = new Bank(1234);
    Konto mockKonto = Mockito.mock(Konto.class);
    Konto mockKonto2 = Mockito.mock(Konto.class);

    @Test
    public void geldAbhebenRuftAbhebenAufTest() throws GesperrtException {
        bank.mockEinfuegen(mockKonto);
        bank.mockEinfuegen(mockKonto2);

        Mockito.when(mockKonto.getKontonummer()).thenReturn(1L);
        Mockito.when(bank.getKontostand(mockKonto.getKontonummer())).thenReturn(10.0);
        bank.geldAbheben(mockKonto.getKontonummer(),10.0);
        Mockito.verify(mockKonto, Mockito.times(1)).abheben(10.0);
    }

    public void geldAbhebenGesperrtExceptionTest() throws GesperrtException {
        bank.mockEinfuegen(mockKonto);
        bank.mockEinfuegen(mockKonto2);

        Mockito.when(mockKonto.getKontonummer()).thenReturn(1L);
        Mockito.when(bank.getKontostand(mockKonto.getKontonummer())).thenReturn(0.0);
        Mockito.doThrow(new GesperrtException(mockKonto.getKontonummer())).when(bank.geldAbheben(mockKonto.getKontonummer(),10.0));
        Mockito.verify(mockKonto, Mockito.times(0)).abheben(10.0);
    }

    @Test
    public void geldEinzahlenRuftEinzahlenAufTest() {
        bank.mockEinfuegen(mockKonto);
        bank.mockEinfuegen(mockKonto2);

        Mockito.when(mockKonto.getKontonummer()).thenReturn(1L);
        Mockito.when(mockKonto2.getKontonummer()).thenReturn(2L);
        Mockito.when(bank.getKontostand(mockKonto.getKontonummer())).thenReturn(10.0);
        Mockito.when(bank.getKontostand(mockKonto2.getKontonummer())).thenReturn(0.0);
        bank.geldEinzahlen(mockKonto2.getKontonummer(), 5.0);
        Mockito.verify(mockKonto2, Mockito.times(1)).einzahlen(5.0);
    }

    @Test
    public void kontoGesperrt() {

    }


//    @Test
//    public void kontoLoeschenTest() {
//        bank.mockEinfuegen(mockKonto);
//        bank.mockEinfuegen(mockKonto2);
//        Mockito.when(mockKonto.getKontonummer()).thenReturn(1L);
//        Mockito.when(mockKonto2.getKontonummer()).thenReturn(2L);
//        Map<Long, Konto> spyKontenliste = Mockito.spy(bank.getKontenliste());
//
//        Mockito.when(bank.kontoLoeschen(mockKonto.getKontonummer())).thenReturn(true);
//        Mockito.verify(spyKontenliste, Mockito.times(1)).remove(mockKonto.getKontonummer());
//
//        Mockito.when(bank.kontoLoeschen(123L)).thenReturn(false);
//        Mockito.verify(spyKontenliste, Mockito.times(0)).remove(123L);
//    }
//
//    @Test
//    public void ueberweisenTest() throws GleichesKontoException, KeinUeberweisungsfaehigesKontoException, GesperrtException {
//        bank.mockEinfuegen(mockKonto);
//        bank.mockEinfuegen(mockKonto2);
//
//        Mockito.when(mockKonto.getKontonummer()).thenReturn(1L);
//        Mockito.when(mockKonto2.getKontonummer()).thenReturn(2L);
//        Mockito.when(bank.getKontostand(mockKonto.getKontonummer())).thenReturn(10.0);
//        Mockito.when(bank.getKontostand(mockKonto2.getKontonummer())).thenReturn(0.0);
//        //Mockito.when(bank.geldUeberweisen(mockKonto.getKontonummer(), mockKonto2.getKontonummer(), 5.0, "Test")).thenReturn(true);
//        //Mockito.verify(mockKonto, Mockito.times(1)).ueberweisungAbsenden()
//    }
//
//    @Test
//    public void ueberweisenGleichesKontoExceptionTest() throws GleichesKontoException, KeinUeberweisungsfaehigesKontoException, GesperrtException {
//        bank.mockEinfuegen(mockKonto);
//
//        Mockito.when(mockKonto.getKontonummer()).thenReturn(1L);
//        Mockito.when(bank.getKontostand(mockKonto.getKontonummer())).thenReturn(10.0);
//        //Mockito.doThrow(new GleichesKontoException("Die Kontonummern beziehen sich auf das gleiche Konto.")).when(bank.geldUeberweisen(mockKonto.getKontonummer(), mockKonto.getKontonummer(), 10.0, "Test"));
//        Mockito.when(bank.geldUeberweisen(mockKonto.getKontonummer(), mockKonto.getKontonummer(), 5.0, "Test")).thenThrow(new GleichesKontoException("Die Kontonummern beziehen sich auf das gleiche Konto."));
//        //Mockito.verify()
//    }
//
//    @Test
//    public void ueberweisenKeinUeberweisungsfaehigesKontoExceptionTest() throws GleichesKontoException, KeinUeberweisungsfaehigesKontoException, GesperrtException {
//        bank.mockEinfuegen(mockKonto);
//
//        Mockito.when(mockKonto.getKontonummer()).thenReturn(1L);
//        Mockito.when(bank.getKontostand(mockKonto.getKontonummer())).thenReturn(10.0);
//        Mockito.doThrow(new KeinUeberweisungsfaehigesKontoException("Die Kontonummern beziehen sich auf das gleiche Konto.")).when(bank.geldUeberweisen(mockKonto.getKontonummer(), mockKonto.getKontonummer(), 10.0, "Test"));
//        //Mockito.when(bank.geldUeberweisen(mockKonto.getKontonummer(), mockKonto.getKontonummer(), 5.0, "Test")).thenThrow(new KeinUeberweisungsfaehigesKontoException("Eines der Konten ist nicht ueberweisungsfaehig."));
//        //Mockito.verify()
//    }

}