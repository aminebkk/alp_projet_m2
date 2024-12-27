package org.example;
import java.util.concurrent.Future;

public interface ObserverDeCapteurAsync {
    Future<Integer> update(Capteur capteur);
}