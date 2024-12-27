package org.example;
public class Afficheur implements ObserverDeCapteur {
    @Override
    public void update(Capteur capteur) {
        System.out.println("Afficheur received value: " + capteur.getValue());
    }
}