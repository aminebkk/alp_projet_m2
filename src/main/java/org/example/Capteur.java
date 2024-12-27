package org.example;
public interface Capteur {
    void attach(ObserverDeCapteurAsync o);
    int getValue();
    void tick();
}
