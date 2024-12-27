import org.example.Afficheur;
import org.example.Canal;
import org.example.CapteurImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import java.util.concurrent.*;
import java.util.List;


public class ProjetObserverTest {

    private CapteurImpl capteur;
    private Afficheur afficheur1;
    private Afficheur afficheur2;
    private Canal canal1;
    private Canal canal2;
    private ScheduledExecutorService executor;

    @BeforeEach
    public void setUp() {
        capteur = new CapteurImpl();
        afficheur1 = new Afficheur();
        afficheur2 = new Afficheur();
        canal1 = new Canal(afficheur1);
        canal2 = new Canal(afficheur2);
        executor = Executors.newScheduledThreadPool(1);
    }

    @Test
    public void testAttachObserver() throws NoSuchFieldException, IllegalAccessException {
        capteur.attach(canal1);

        // Utilisation de reflection pour accéder au champ observers
        Field observersField = CapteurImpl.class.getDeclaredField("observers");
        observersField.setAccessible(true);  // Rendre le champ accessible
        @SuppressWarnings("unchecked")
        List<Canal> observers = (List<Canal>) observersField.get(capteur);

        Assertions.assertEquals(1, observers.size(), "Observer should be attached");

        // Vérifier que l'exception est levée lors d'une tentative d'attachement du même observateur
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            capteur.attach(canal1);
        });
        Assertions.assertEquals("Observer already attached", exception.getMessage(), "Exception message should match");
    }

    @Test
    public void testTickAndNotifyObservers() throws InterruptedException {
        capteur.attach(canal1);
        capteur.attach(canal2);

        // Simuler un CountDownLatch pour s'assurer que les deux observateurs sont notifiés
        CountDownLatch latch = new CountDownLatch(2); // Nombre d'observateurs

        // Mock des objets Afficheur pour vérifier si update() est appelé
        Afficheur mockAfficheur1 = Mockito.mock(Afficheur.class);
        Afficheur mockAfficheur2 = Mockito.mock(Afficheur.class);
        Canal mockCanal1 = new Canal(mockAfficheur1);
        Canal mockCanal2 = new Canal(mockAfficheur2);

        // Attacher les observateurs
        capteur.attach(mockCanal1);
        capteur.attach(mockCanal2);

        // Simuler que la méthode update() des afficheurs invoque latch.countDown()
        Mockito.doAnswer(invocation -> {
            latch.countDown(); // Compte à rebours sur chaque appel de update
            return null;
        }).when(mockAfficheur1).update(Mockito.any(CapteurImpl.class));
        Mockito.doAnswer(invocation -> {
            latch.countDown(); // Compte à rebours sur chaque appel de update
            return null;
        }).when(mockAfficheur2).update(Mockito.any(CapteurImpl.class));

        // Exécuter le tick
        capteur.tick();

        // Attendre que les deux observateurs aient été notifiés
        latch.await(1, TimeUnit.SECONDS);

        // Vérifier que les observateurs ont bien été notifiés
        Assertions.assertEquals(0, latch.getCount(), "Both observers should be notified");

        // Vérifier que la valeur du capteur a bien augmenté
        Assertions.assertEquals(1, capteur.getValue(), "Capteur value should be incremented after tick");
    }

    @Test
    public void testAsyncUpdate() throws ExecutionException, InterruptedException {
        capteur.attach(canal1);

        // Déclencher la mise à jour asynchrone
        Future<Integer> future = canal1.update(capteur); // Trigger async update
        Integer value = future.get(); // Attendre le résultat

        // Vérifier que la valeur retournée par l'async est correcte
        Assertions.assertEquals(capteur.getValue(), value, "Async update should return the correct value");
    }

    @Test
    public void testShutdown() throws NoSuchFieldException, IllegalAccessException {
        canal1.shutdown();
        canal2.shutdown();

        // Utiliser la réflexion pour accéder au champ 'executor' dans Canal
        Field executorField1 = Canal.class.getDeclaredField("executor");
        executorField1.setAccessible(true);  // Rendre le champ accessible
        ScheduledExecutorService executor1 = (ScheduledExecutorService) executorField1.get(canal1);

        Field executorField2 = Canal.class.getDeclaredField("executor");
        executorField2.setAccessible(true);  // Rendre le champ accessible
        ScheduledExecutorService executor2 = (ScheduledExecutorService) executorField2.get(canal2);

        // Vérifier que les exécuteurs sont bien arrêtés
        Assertions.assertTrue(executor1.isShutdown(), "Canal 1 executor should be shut down");
        Assertions.assertTrue(executor2.isShutdown(), "Canal 2 executor should be shut down");
    }

    @Test
    public void testMainShutdown() {
        // Cette méthode ne peut pas être testée directement sans exécuter la JVM
        // mais on peut vérifier la logique de shutdown dans les tests précédents.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            executor.shutdown();
            canal1.shutdown();
            canal2.shutdown();
        }));
    }

    @Test
    public void testAttachMultipleObservers() throws NoSuchFieldException, IllegalAccessException {
        capteur.attach(canal1);
        capteur.attach(canal2);

        // Utilisation de reflection pour accéder au champ observers
        Field observersField = CapteurImpl.class.getDeclaredField("observers");
        observersField.setAccessible(true);  // Rendre le champ accessible
        @SuppressWarnings("unchecked")
        List<Canal> observers = (List<Canal>) observersField.get(capteur);

        // Vérifier que les deux canaux sont bien attachés au capteur
        Assertions.assertEquals(2, observers.size(), "There should be two observers attached");
    }
}
