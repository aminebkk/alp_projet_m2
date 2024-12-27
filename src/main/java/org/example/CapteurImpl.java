package org.example;
import java.util.*;

public class CapteurImpl implements Capteur {
    private final List<ObserverDeCapteurAsync> observers = new ArrayList<>();
    private int value = 0;

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void attach(ObserverDeCapteurAsync observer) {
        Objects.requireNonNull(observer, "Observer cannot be null");
        if (observers.contains(observer)) {
            throw new IllegalArgumentException("Observer already attached");
        }
        observers.add(observer);
    }

    @Override
    public void tick() {
        value++;
        notifyObservers();
    }

    private void notifyObservers() {
        for (ObserverDeCapteurAsync observer : observers) {
            observer.update(this);
        }
    }
}
