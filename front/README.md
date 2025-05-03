# Yoga App — Front-end

Ce module Angular contient l’interface utilisateur de l’application Yoga.

---

## Prérequis

- Node.js ≥ 16
- npm (ou yarn)
- Angular CLI 14

---

## Installation

cd front
npm install

## Lancement en mode développement

cd front
npm run start

- L’application sera disponible sur http://localhost:4200.

- Par défaut, elle interroge l’API à http://localhost:8080/api

## Tests unitaires & d’intégration

    Unitaires (Jest) et intégration :

cd front
npm run test

Watch mode :

cd front
npm run test:watch

Rapport de couverture (Jest) :
Après npm run test, ouvrez front/coverage/jest/lcov-report/index.html

Tests end-to-end (E2E)

Nous utilisons Cypress pour les tests e2e.

    Ouvrir Cypress pour les tests :

cd front
npm run e2e

Générer le rapport de couverture E2E :

cd front
npm run e2e:coverage

Exécuter tous les tests afin d'englober tout le couverage :

cd front
npx cypress run

Le rapport de couverture E2E est généré dans front/coverage-e2e/index.html

Ne pas oublier de faire npx cypress run avant de consulter la page de coverage

Variables d’environnement

Les URLs d’API et autres paramètres sont définis dans src/environments/*.ts

