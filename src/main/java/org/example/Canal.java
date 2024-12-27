package org.example;
import java.util.concurrent.*;

public class Canal implements ObserverDeCapteurAsync {
    private final ScheduledExecutorService executor;
    private final ObserverDeCapteur afficheur;

    public Canal(ObserverDeCapteur afficheur) {
        this.executor = Executors.newScheduledThreadPool(1);
        this.afficheur = afficheur;
    }

    @Override
    public Future<Integer> update(Capteur capteur) {
        return executor.schedule(new Update(capteur, afficheur), 500, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
