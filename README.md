# OC-Bibliothèque V1.1.0

## Présentation

Ce projet est un fork du projet OC-bibliothèque réalisé dans le cadre du projet 7. L'objectif est de reprendre le travail fait sur la version 1 du projet livrée pour y apporter des améliorations et amener des correctifs.

Les compétences évaluées sont les suivantes :
* Apporter des améliorations de fonctionnalités demandées par le client
* Compléter une suite de tests unitaires et d’intégration afin de prendre en compte les modifications apportées
* Corriger des dysfonctionnements signalés par le client sur l’application

## Modifications apportées

* Ticket#1 - Ajout d'une fonctionnalité de réservation d'ouvrages
* Ticket#2 - Correction d'un bug dans la gestion des prolongations de prêt
* Ticket#3 - Mise en place d'une stratégie de tests

## Guide de démarrage

### Prérequis

* _Java-8_ et plus, disponible [ici](https://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)
* _SpringBoot-2.2.1_ et plus, disponible [ici](https://start.spring.io/)
* _PostgreSQL-11_ et plus, système de gestion de base de données, disponible [ici](https://www.postgresql.org/download/).  
* _pgAdmin-4_ et plus, outil d'administration de PostgreSQL, disponible [ici](https://www.pgadmin.org/download/).
* _Maven-3.6.1_ et plus, outil de gestion et d'automatisation de production des projets logiciels, disponible [ici](https://maven.apache.org/download.cgi)
* Le répertoire du projet, disponible [ici](https://github.com/ccathala/OC-Bibliotheque)
* _FakeSmtp-2.0_ et plus, emulateur de serveur smtp pour tester l'envoi de mails, disponible [ici](http://nilhcem.com/FakeSMTP/download.html)

### Paramétrage

Chaque service dispose à l'intérieur de son répertoire, à l'emplacement /src/main/resources d'un fichier application.properties. Celui-ci permet de paramétrer certaines propriétés:

#### Port

```properties
server.port=9001
```

#### Nom de l'application

```properties
spring.application.name=oc-bibliotheque-api
```

#### Données de connexion à la base de données (API seulement)

```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:postgresql://localhost:5432/OC_bibliotheque
spring.datasource.username=postgres
spring.datasource.password=azerty
```

#### Identifiants Basic Auth (API seulement)

```properties
spring.security.user.name=OCBibliotheque-client
spring.security.user.password=OCB2020
```

#### Nom et URL de l'API (Webapp et Batch seulement)

```properties
api.name=oc-bibliotheque-api
api.url=localhost:9001
```

#### Paramètres SSL (Webapp seulement)

```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:ocbclient.p12
server.ssl.key-store-password=azerty
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=ocbclient
```

#### Cron expression (Batch seulement)

```properties
batch.time.event.lateborrows=30 21 17 * * ?
batch.time.event.reservationsnotifications=30 45 10 * * ?
```

Exemple fonction mail de relance :

Seconde=30  
Minute=45  
Heure=10

 L'envoie des mails de relance ce fera tous les jours à 10h 45min 30s

#### Configuration du compte SMTP

##### Fake SMTP

```properties
spring.mail.host=localhost
spring.mail.port=25
```

##### Compte Gmail

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=username
spring.mail.password=password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Remplacer les valeurs username et password par les identifiants du compte Gmail.

***Attention un mot de passe d'application est nécessaire, plus d'information [ici](https://support.google.com/mail/answer/185833?hl=fr)***

### Démarrage

#### I. Tests

##### A. API

Les tests de l'API sont répartis en 2 catégories :
* Les tests unitaires
* Les tests d'integrations

###### 1. Répertoires de test

* Les classes de tests unitaires se trouvent dans le répertoire src/test
* Les classes de tests d'integrations se trouvent dans le répertoire src/test-integration

###### 2. Profils

Un profil Maven test-integration est déclaré dans le pom.xml, il définit en tant que répertoire de test l'emplacement src/test-integration.

###### 3. Paramétrage du Datasource pour les tests d'integrations

Le Datasource est paramétré via le fichier application.properties à l'emplacement src/test-integration/resources.

La clé spring.profiles.active permet de déclarer le DataSource à utiliser.

```properties
spring.profiles.active=dev
```
Pour utiliser le Datasource défini dans le fichier application-dev.properties qui correspond à l'utilisation de la base de données dockerisée pour l'intégration continue.

```properties
spring.profiles.active=integration
```
Pour utiliser le Datasource défini dans le fichier application-integration.properties qui correspond à l'utilisation de la base de données locale.

###### 4. Commandes

1. Ouvrez un terminal à l'emplacement /api

2. Lancer l'une des commande de test suivante:

Test unitaires:
```terminal
mvn test
```

Test unitaires + tests d'integrations :
```terminal
mvn test -Ptest-integration
```

Test unitaires + tests d'integrations + rapport de couverture de code :
```terminal
mvn verify -Ptest-integration
```

##### B. Module webapp et batch

1. Ouvrez le répertoire du service choisi, ex : /webapp ou /batch

2. Ouvrez un terminal à cet emplacement

3. Exécuter la commande suivante :

```terminal
mvn test
```

Cette commande va compiler le code et lancer la séquence de test

#### II. Développement

1. Ouvrez le répertoire du service choisi, ex: /api

2. Ouvrez un terminal à cet emplacement

3. Exécuter la commande suivante:

```terminal
mvn spring-boot:run
```

Cette commande va compiler le code et lancer la séquence de test, générer le package .jar et lancer l'application sous Tomcat.

#### III. Production

1. Ouvrez le répertoire du service choisi, ex: /api

2. Ouvrez un terminal à cet emplacement

3. Exécuter la commande suivante:

```terminal
java -jar application.jar
```

Cette commande va compiler le code et lancer la séquence de test, générer le package .jar et lancer l'application sous Tomcat.

Remplacez application.jar par le nom de l'application à lancer.

### Import des données de démonstration

1. Lancer une première fois le service api pour générer les tables dans la base de données.

2. À l'aide de pgAdmin, éxécuter le script sql _data_demo_v1.1.0.sql_.

Ce fichier se trouve dans le répertoire /database/v1.1.0

### Migration des données v1.0.0 ==> v1.1.0

À l'aide de pgAdmin, éxécuter les script sql suivants dans cet ordre :

1. _update_table.sql_ (mise à jour les tables)
2. _update_data_demo.sql_ (mise à jour des données de démo)

Ces fichiers se trouvent dans le répertoire /database/migration_v1.0.0_to_v1.1.0

### Utilisateurs enregistrés

Identifiants :
 * ccathala.dev@gmail.com  
 * agetten.dev@gmail.com
 * dvalat.dev@gmail.com
 * slassy.dev@gmail.com
 
Mot de passe : azerty

## Technologies utilisées

* JEE
* Spring
  * Spring Boot
  * Spring Data JPA
  * Spring Security
  * Spring MVC
* Rest
* OpenFeign
* Swagger
* Thymleaf
* SSl
* Bootstrap
* Maven

## Aperçu du site

URL : https://localhost:9002

![site_sample](site_sample.png)

## Auteur

* **Charles Cathala** - [ccathala](https://gist.github.com/ccathala)
