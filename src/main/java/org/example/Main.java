package org.example;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Afficheur afficheur1 = new Afficheur();
        Afficheur afficheur2 = new Afficheur();

        Canal canal1 = new Canal(afficheur1);
        Canal canal2 = new Canal(afficheur2);

        // Capteur capteur = new CapteurImpl();
        List<ObserverDeCapteurAsync> canaux = new ArrayList<>();
        canaux.add(new Canal(afficheur1));
        canaux.add(new Canal(afficheur2));

        Capteur capteur = new CapteurImpl();
        for (ObserverDeCapteurAsync c : canaux) {
            capteur.attach(c);
        }
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = capteur::tick;
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            executor.shutdown();
            canal1.shutdown();
            canal2.shutdown();
        }));

    }
}