# Yoga App — Back-end

Cette API Spring Boot fournit les endpoints REST pour l’application Yoga.

---

## Prérequis

- Java 11 (ou supérieur)
- Maven 3.6+
- MySQL (port 3306)

---

## Installation et configuration

1. **Cloner le repo** et aller dans le dossier back :

   cd back

2. Configurer la base de données

   Créez une base yoga_db

   Mettez vos identifiants dans src/main/resources/application.properties ou via variables d’environnement :

spring.datasource.url=jdbc:mysql://localhost:3306/yoga_db?serverTimezone=UTC&createDatabaseIfNotExist=true
spring.datasource.username=VOTRE_USER
spring.datasource.password=VOTRE_PASS
oc.app.jwtSecret=VOTRE_CLE_SECRETE
oc.app.jwtExpirationMs=86400000

Charger le schéma SQL (optionnel si createDatabaseIfNotExist=true)
Fichier disponible dans ressources/sql/script.sql

Je vous recommande d'utiliser Heidi SQL afin d'executer le fichier

3. **Lancer l’application** :

  
   mvn spring-boot:run
   

Tests
Tests unitaires & d’intégration (JUnit + Mockito)

(N'oubliez pas de mettre le coverage du pom.xml à 0 avant de faire un premier mvn clean install)

cd back
mvn clean install
mvn clean test

Rapport de couverture (JaCoCo) généré dans : back/target/site/jacoco/index.html

    Seuils JaCoCo :

        90 % de lignes couvertes par package (hors dto et mapper).

        DTO simples exclus du check.

        Le seuil est modifiable dans le fichier pom.xml.

Dépendances clés

- Spring Boot Starter Web, Data JPA, Security, Validation

- MySQL Connector

- jjwt (JSON Web Token)

- MapStruct (mapping DTO ↔ Entity)

- Lombok (facultatif)

- JUnit 5, Mockito, Spring-security-test

- JaCoCo pour la couverture de code



