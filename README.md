# API de Gestion de Fichiers avec MinIO

API REST développée en **Spring Boot** pour gérer des fichiers (upload, téléchargement, liste) dans un **bucket MinIO**.

## Table des matières

- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Endpoints](#endpoints)
- [Exemples d’utilisation](#exemples-dutilisation)
- [Gestion des erreurs](#gestion-des-erreurs)
- [Bonnes pratiques](#bonnes-pratiques)

---

## Prérequis

- Java 17 ou supérieur
- Maven 3.x
- MinIO (local ou distant)
- Postman, Bruno ou cURL pour tester l’API

---

## Installation

1. Cloner le projet :

```bash
git clone <URL_DU_REPOSITORY>
cd demominio