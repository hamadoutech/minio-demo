# API de Gestion de Fichiers avec MinIO

API REST développée en **Spring Boot** pour gérer des fichiers (upload, téléchargement, liste, suppression) dans un **bucket MinIO**. Cette API est documentée avec **Swagger UI** pour faciliter son utilisation.

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
- Docker et Docker Compose (pour exécuter MinIO en local)
- Postman, Bruno ou cURL pour tester l’API

---

## Installation

1. Cloner le projet :

```bash
git clone <URL_DU_REPOSITORY>
cd demominio
```

2. Construire le projet avec Maven :

```bash
mvn clean install
```

3. Lancer le serveur MinIO avec Docker Compose :

```bash
cd src/main/resources/docker
docker-compose up -d
```

4. Démarrer l’application Spring Boot :

```bash
mvn spring-boot:run
```

---

## Configuration

La configuration de l’application se fait dans le fichier `application.yaml` situé dans le dossier `src/main/resources`.

### Exemple de configuration pour MinIO :

```yaml
minio:
  endpoint: http://localhost:9000
  access-key: minioadmin
  secret-key: minioadmin
  bucket-name: my-bucket
```

---

## Endpoints

### Liste des fichiers
- **GET** `/v1/minio/list`
- **Description** : Récupère la liste de tous les fichiers dans le bucket MinIO.

### Télécharger un fichier
- **GET** `/v1/minio/download/{fileName}`
- **Description** : Télécharge un fichier spécifique depuis le bucket MinIO.

### Uploader un fichier
- **POST** `/v1/minio/upload`
- **Description** : Charge un fichier dans le bucket MinIO.
- **Paramètres** :
  - `file` : Fichier à uploader (obligatoire)

### Supprimer un fichier
- **DELETE** `/v1/minio/delete/{fileName}`
- **Description** : Supprime un fichier spécifique du bucket MinIO.

---

## Exemples d’utilisation

### Uploader un fichier avec cURL

```bash
curl -X POST -F "file=@chemin/vers/fichier.txt" http://localhost:8080/v1/minio/upload
```

### Télécharger un fichier avec cURL

```bash
curl -X GET http://localhost:8080/v1/minio/download/nom_du_fichier -o nom_du_fichier
```

### Supprimer un fichier avec cURL

```bash
curl -X DELETE http://localhost:8080/v1/minio/delete/nom_du_fichier
```

---

## Gestion des erreurs

L’API retourne des codes d’erreur HTTP standard :

- **200 OK** : Requête réussie
- **400 Bad Request** : Requête invalide (ex. fichier manquant ou non supporté)
- **404 Not Found** : Ressource non trouvée (ex. fichier inexistant)
- **500 Internal Server Error** : Erreur interne du serveur

---

## Bonnes pratiques

- Utiliser des noms de fichiers uniques pour éviter les conflits.
- Configurer des règles de cycle de vie pour nettoyer automatiquement les fichiers obsolètes dans MinIO.
- Sécuriser l’accès à l’API avec un mécanisme d’authentification (ex. JWT).
- Tester les endpoints avec des outils comme Postman ou cURL.

## Swagger UI
Interface Swagger UI : http://localhost:8080/swagger-ui/index.html ou http://localhost:8080/swagger-ui.html
Documentation OpenAPI JSON : http://localhost:8080/v3/api-docs
