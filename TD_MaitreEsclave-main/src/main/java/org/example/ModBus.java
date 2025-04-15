package org.example;

import LiaisonSerie.LiaisonSerie;
import ModBusRtu.CRC16;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import org.example.clavier.In;

import java.util.Arrays;

public class ModBus extends LiaisonSerie {
    CRC16 crc16 = new CRC16();
    Byte numeroEsclave;
    byte[] tramWithCRC16;

    public ModBus(Byte numeroEsclave) {
        this.numeroEsclave = numeroEsclave;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void ModeBus() {
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void fermerLiasonSerie() {
        super.fermerPort();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public byte[] intDeuxBytes(int number) {
        byte[] deuxBytes = new byte[2];
        deuxBytes[0] = (byte) ((number & 0xFF00) >> 8);
        deuxBytes[1] = (byte) (number & 0xFF);
        return deuxBytes;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void connecterEsclave(String port, int vitesse, int donnees, int parite, int stop) throws SerialPortException {
        super.initCom(port);
        super.configurerParametres(vitesse, donnees, parite, stop);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void serialEvent(SerialPortEvent event) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        byte[] trame = super.lireTrame(super.detecteSiReception());
        if (trame != null) {
            System.out.println("Réponse reçue : " + formatReponseHexa(trame));
            float valeur = decoderReponse(trame);  // Appel de la méthode de décodage
            System.out.println("Valeur décodée : " + valeur / 10);
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public float lectureCoils() throws InterruptedException {
        System.out.println("Veuillez entrer la trame RTU (en hexadécimal, sans espaces) :");
        String inputTrame = In.readString();

        assert inputTrame != null;
        byte[] tramWithCRC16 = new byte[inputTrame.length() / 2];
        for (int i = 0; i < tramWithCRC16.length; i++) {
            tramWithCRC16[i] = (byte) Integer.parseInt(inputTrame.substring(2 * i, 2 * i + 2), 16);
        }
        super.ecrire(tramWithCRC16);
        Thread.sleep(1000);

        if (detecteSiReception() > 0) {
            byte[] reponse = super.lireTrame(detecteSiReception());
            return decoderReponse(reponse);
        } else {
            return 0f;
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    private String formatReponseHexa(byte[] reponse) {
        StringBuilder sb = new StringBuilder();
        for (byte b : reponse) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }






    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    private float decoderReponse(byte[] reponse) {
        if (reponse.length < 5) {
            System.out.println("Trame reçue invalide.");
            return 0f;
        }

        if (reponse[1] == 0x03) {
            byte[] donnees = Arrays.copyOfRange(reponse, 3, reponse.length - 2);

            int valeurRegistre = (donnees[0] << 8) | (donnees[1] & 0xFF);

            return (float) valeurRegistre;
        } else {
            System.out.println("Code fonction incorrect.");
            return 0f;
        }
    }



                    //////////////////////////// LECTURE VALEUR ////////////////////////////



    private float lectureValeur(String inputTrame) throws InterruptedException {

        assert inputTrame != null;
        byte[] tramWithCRC16 = new byte[inputTrame.length() / 2];
        for (int i = 0; i < tramWithCRC16.length; i++) {
            tramWithCRC16[i] = (byte) Integer.parseInt(inputTrame.substring(2 * i, 2 * i + 2), 16);
        }
        super.ecrire(tramWithCRC16);
        Thread.sleep(1000);

        if (detecteSiReception() > 0) {
            byte[] reponse = super.lireTrame(detecteSiReception());
            return decoderReponse(reponse);
        } else {
        return 0f;
    }
}




                        //////////////////////////// FREQUENCE VOLT ////////////////////////////



    public float lectureFrequence() throws InterruptedException {
        System.out.println("Ecriture de la trame pour recevoir la FREQUENCE");
        return lectureValeur("010300000001840A");
    }



                        //////////////////////////// TENSION HZ ////////////////////////////



    public float lectureTension() throws InterruptedException {
        System.out.println("Ecriture de la trame pour recevoir la TENSION");
        return lectureValeur("0103000F0001B409");
    }



                        //////////////////////////// PUISSANCE KW ////////////////////////////



    public float lecturePuissance() throws InterruptedException {
        System.out.println("Ecriture de la trame pour recevoir la PUISSANCE");
        return lectureValeur("01030010000185CF");
    }



                 //////////////////////////// INTENSITE AMPERE ////////////////////////////



    public float lectureIntensite() throws InterruptedException{
        System.out.println("Ecriture de la trame pour recevoir l'INTENSITE");
        return lectureValeur("01030002000125CA");
    }


/*    public float lectureIntensite() throws InterruptedException {
        System.out.println("Ecriture de la trame pour recevoir l'INTENSITE");
        float valeur = lectureValeur("01030002000125CA");
        valeur /= 1000;
        System.out.println("Valeur décodée : " + valeur + " Ampère");
        return valeur;
    }*/

}
