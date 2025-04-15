package org.example;

import LiaisonSerie.LiaisonSerie;
import jssc.SerialPortException;
import org.example.clavier.In;

public class Main {
    public static void main(String[] args) throws InterruptedException, SerialPortException {
        System.out.println("Le numero de l'esclave : ");
        org.example.ModBus modbus ;
        modbus= new org.example.ModBus(In.readByte());
        System.out.println("Le COM ?");
        modbus.connecterEsclave(In.readString(),9600,8,0,1);
        try {
            while (true) {
                try {
                    float resultatFrequence = modbus.lectureFrequence();
                    float resultatTension = modbus.lectureTension();
                    float resultatPuissance = modbus.lecturePuissance();
                    float resultatIntensite = modbus.lectureIntensite();


                    /*System.out.println("Résultat de la Tension : " + resultatTension);
                    System.out.println("Résultat de la Frequence : " + resultatFrequence);*/



                    Thread.sleep(60000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
}