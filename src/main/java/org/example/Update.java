package org.example;
import java.util.concurrent.Callable;

public class Update implements Callable<Integer> {
    private final Capteur capteur;
    private final ObserverDeCapteur afficheur;

    public Update(Capteur capteur, ObserverDeCapteur afficheur) {
        this.capteur = capteur;
        this.afficheur = afficheur;
    }

    @Override
    public Integer call() {
        try {
            afficheur.update(capteur);
            return capteur.getValue();
        } catch (Exception e) {
            System.err.println("Error during update: " + e.getMessage());
            return null;
        }
    }
}
