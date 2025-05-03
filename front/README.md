# Yoga-App — Application de réservation de sessions de yoga

> **Testez une application full-stack**  
> Front-end : Angular 14 • Back-end : Spring Boot • Base : MySQL 5+  
> Tests : Jest, Cypress, JUnit 5 + Mockito, JaCoCo

---

## 🗂️ Structure du projet

## 🗂️ Structure du projet

yoga-app/
├─ back/ # API Spring Boot
│ ├─ src/
│ ├─ pom.xml
│ └─ README-back.md # (spécifique back)
├─ front/ # SPA Angular
│ ├─ src/
│ ├─ cypress/ # tests E2E
│ └─ package.json
├─ ressources/
│ ├─ postman/
│ │ └─ yoga.postman_collection.json
│ └─ sql/
│ └─ script.sql
├─ .github/workflows/ci.yml # pipeline CI
└─ README.md # ce fichier<


---

## 🚀 Prérequis

- **Java 11+** & **Maven**
- **Node 16+**, **npm**
- **Angular CLI 14** (`npm install -g @angular/cli@14`)
- **MySQL 5.7+** (port 3306)
- (Facultatif) IDE : VS Code, IntelliJ…

---

## 🛠️ Installation & configuration

1. **Cloner le repo**  
   ```bash
   git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing.git yoga-app
   cd yoga-app

2. **Base de données MySQL**

    Importer /ressources/sql/script.sql ou laisser createDatabaseIfNotExist=true créer la base yoga_db.

    Par défaut, admin :

    login : yoga@studio.com  
    password : test!1234

    Variables d’environnement Back
Dans back/src/main/resources/application.properties (ou via env vars) :

    spring.datasource.url=jdbc:mysql://localhost:3306/yoga_db?createDatabaseIfNotExist=true&serverTimezone=UTC
    spring.datasource.username=VOTRE_DB_USER
    spring.datasource.password=VOTRE_DB_PASS

    oc.app.jwtSecret=VOTRE_CLE_SECRETE_JWT
    oc.app.jwtExpirationMs=86400000

▶️ Démarrage
1. Lancer l’API (Back-end)

cd back
mvn clean install
mvn spring-boot:run

L’API REST est disponible sur http://localhost:8080/api
2. Lancer le Front-end

cd front
npm install
ng serve

Le client web tourne sur http://localhost:4200
🧪 Tests & couverture
A. Tests unitaires & d’intégration Front (Jest)

cd front
npm test
# ↪ lance les tests unitaires (*.spec.ts) + d’intégration (*.integration.spec.ts)
npm run test:coverage
# ↪ génère rapport dans coverage/front/index.html

    Objectif : ≥ 80 % de couverture, ≥ 30 % en intégration

    Mock des services avec HttpClientTestingModule ou HttpClientInMemoryWebApiModule

B. Tests End-to-End (Cypress)

cd front
npx cypress open      # interface interactive
npx cypress run       # exécution headless
npm run e2e           # alias pour npx cypress run
npm run e2e:coverage  # génère rapport avec @cypress/code-coverage

    Mock API via cy.intercept()

    Objectif : ≥ 80 % de couverture E2E

C. Tests Back (JUnit 5 + Mockito + SpringBootTest)

cd back
mvn test
mvn jacoco:report

    Unitaires : services & contrôleurs (MockMvc + Mockito)

    Intégration : @SpringBootTest + H2 + TestRestTemplate

    Objectif : ≥ 80 % de couverture, ≥ 30 % en intégration

    Ne pas tester les DTO

📊 Rapports de couverture

    Front : front/coverage/jest/index.html

    E2E : front/coverage/e2e/index.html

    Back : back/target/site/jacoco/index.html

