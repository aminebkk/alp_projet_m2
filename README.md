# Projet-ALP : Système de Surveillance de Capteur
Ce projet simule un système de surveillance de **Capteur** (capteur) avec des observateurs qui réagissent aux changements des données du capteur. Le système est conçu pour démontrer le **modèle de conception Observer**, les **mises à jour asynchrones** et les **stratégies de test**.

## Fonctionnalités

- **Capteur (Sensor)** : Le composant principal qui produit les données du capteur.
- **Modèle Observer** : Mise en œuvre d'un système d'observateurs qui réagissent aux mises à jour des données du capteur.
- **Observateurs Asynchrones** : Utilisation de tâches asynchrones pour simuler les mises à jour en temps réel des capteurs.
- **Tâches Planifiées** : Le capteur se met à jour périodiquement en utilisant un exécutant planifié.
- **Observateurs** : Plusieurs observateurs (`Canal`, `Afficheur`) consomment et affichent les données.

## Concepts Abordés

### 1. **Les Stratégies**

Dans ce projet, nous appliquons le **modèle de conception Strategy** pour gérer différents types d'observateurs et la manière dont ils traitent les données. Les observateurs peuvent être mis à jour de manière asynchrone, en utilisant des délais aléatoires pour simuler un comportement en temps réel. Le **Strategy** permet une extensibilité future pour ajouter de nouveaux comportements d'observateurs sans changer la logique principale.

#### Exemple d'application de la stratégie :

Les observateurs (`Canal`) peuvent implémenter différentes stratégies de mise à jour. Par exemple :
- **Récupération des données en temps réel** : Les observateurs peuvent récupérer immédiatement les données après une mise à jour.
- **Récupération des données avec retard** : Les observateurs récupèrent les données après un certain délai, simulant un flux de données lent.

Ce modèle permet une grande flexibilité pour ajouter de nouvelles stratégies concernant la manière dont les observateurs interagissent avec le capteur.

### 2. **Les Oracles**

Un **oracle** dans ce projet fait référence à un mécanisme permettant de vérifier que les observateurs reçoivent correctement les données mises à jour du capteur.

Dans notre cas, un oracle pourrait être :
- **Une vérification lisible par un humain** : Simplement imprimer les valeurs reçues par chaque observateur (`Canal` et `Afficheur`), et vérifier manuellement si les valeurs attendues correspondent aux valeurs imprimées.

## Diagrammes UML

### Diagramme de Classes
https://github.com/aminebkk/alp_projet_m2/blob/master/Class_Dgrm.png

### Diagramme de Séquence
![Diagramme de Séquence][(https://github.com/aminebkk/alp_projet_m2/blob/master/sequence_dgrm.png)]

## Tests

Une suite de tests unitaires a été mise en place pour vérifier la robustesse et la cohérence du système. Les tests utilisent JUnit et Mockito, et couvrent plusieurs scénarios pour valider le comportement du système.

### Scénarios Testés

1. **Attach Observer**  
   Vérifie que les observateurs sont correctement attachés au capteur et qu'une exception est levée si un observateur est déjà attaché.

2. **Tick et Notification des Observateurs**  
   Simule une mise à jour du capteur (`tick`) et vérifie que tous les observateurs sont notifiés correctement.

3. **Mise à Jour Asynchrone**  
   Teste la fonctionnalité de mise à jour asynchrone des observateurs.

4. **Fermeture (Shutdown)**  
   Vérifie que les ressources liées aux observateurs (par exemple, les exécuteurs planifiés) sont correctement libérées après fermeture.

5. **Attach Multiple Observers**  
   Teste le comportement lorsque plusieurs observateurs sont attachés au capteur.

6. **Shutdown Global**  
   Simule l'arrêt complet de l'application et vérifie que tous les composants sont correctement arrêtés.

### Exemple de Code de Test

```java
@Test
public void testAttachObserver() throws NoSuchFieldException, IllegalAccessException {
    capteur.attach(canal1);
    
    Field observersField = CapteurImpl.class.getDeclaredField("observers");
    observersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<Canal> observers = (List<Canal>) observersField.get(capteur);
    
    assertEquals(1, observers.size(), "Observer should be attached");
}
```
### Instructions d'exécution

#### Étapes pour exécuter l'application

1. Clonez ce dépôt sur votre machine locale.
2. Assurez-vous que **Java 11** ou une version supérieure est installé.
3. Naviguez vers le répertoire du projet dans votre terminal.
4. Compilez le projet avec Maven :

   ```bash
   mvn clean install
   ```

5. Exécutez l'application avec la commande suivante :

   ```bash
   java -jar target/projet-alp.jar
   ```

#### Dépendances nécessaires

- **Java 11** ou version supérieure
- **Maven** pour la gestion des dépendances et la compilation

#### Commandes pour exécuter l'application

Une fois l'application compilée avec Maven, vous pouvez l'exécuter avec la commande suivante :

```bash
java -jar target/projet-alp.jar
```

Cela exécutera le fichier `.jar` généré et lancera le système de surveillance du capteur avec les observateurs en action.

## Structure du projet

Le projet est organisé de la manière suivante :

```
projet-alp/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── (classes Java)
│   │   └── resources/
│   │       └── (fichiers de configuration, etc.)
│   └── test/
│       └── java/
│           └── (test unitaires)
├── target/
│   └── (fichiers compilés et .jar)
├── pom.xml
└── README.md
```

- **src/main/java** : Contient le code source Java principal.
- **src/test/java** : Contient les tests unitaires.
- **target** : Répertoire généré par Maven contenant les artefacts compilés.
- **pom.xml** : Le fichier de configuration de Maven, où sont spécifiées les dépendances et les plugins.
- **README.md** : Ce fichier, qui fournit des informations sur le projet.

## Contribuer

Si vous souhaitez contribuer à ce projet, vous pouvez suivre ces étapes :

1. Fork ce dépôt.
2. Créez une nouvelle branche (`git checkout -b ma-nouvelle-fonctionnalite`).
3. Faites vos changements et commitez-les (`git commit -am 'Ajout d'une nouvelle fonctionnalité'`).
4. Poussez la branche (`git push origin ma-nouvelle-fonctionnalite`).
5. Ouvrez une Pull Request.
