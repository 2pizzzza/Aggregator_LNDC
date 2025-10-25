# Aggregator Preisvergleich

Dieses Projekt ist eine Spring-Boot-Anwendung, die als Preisaggregator für verschiedene Geschäfte dient. Sie ermöglicht es Benutzern, nach Produkten zu suchen und die Preise und Verfügbarkeit in verschiedenen Geschäften zu vergleichen.

## Inhaltsverzeichnis

- [Funktionen](#funktionen)
- [Technologien](#technologien)
- [Voraussetzungen](#voraussetzungen)
- [Installation](#installation)
- [Konfiguration](#konfiguration)
- [Anwendung starten](#anwendung-starten)
- [API-Endpunkte](#api-endpunkte)
- [Datenbank](#datenbank)
- [Datenimport](#datenimport)

## Funktionen

- Suchen Sie nach Produkten in mehreren Geschäften.
- Filtern Sie Produkte nach Preis, Größe, Farbe und Geschlecht.
- Suchen Sie nach Geschäften in einem bestimmten Radius basierend auf dem Standort des Benutzers.
- Paginierung für Produktlisten.
- API-Dokumentation mit Swagger.

## Technologien

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Flyway
- Maven
- Lombok
- ModelMapper
- Swagger (OpenAPI)

## Voraussetzungen

- JDK 21 oder höher
- Maven 3.6 oder höher
- PostgreSQL 13 oder höher

## Installation

1.  **Klonen Sie das Repository:**
    ```bash
    git clone https://github.com/2pizzzza/Aggregator_LNDC.git
    cd Aggregator_LNDC
    ```

2.  **Erstellen Sie die PostgreSQL-Datenbank:**
    ```sql
    CREATE DATABASE aggregator;
    ```

## Konfiguration

Die Anwendungskonfiguration befindet sich in der Datei `src/main/resources/application.properties`.

1.  **Datenbankverbindung:**
    Passen Sie die folgenden Eigenschaften an Ihre PostgreSQL-Konfiguration an:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/aggregator
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```

2.  **Flyway-Konfiguration:**
    Es wird empfohlen, den Speicherort der Flyway-Migrationen auf den Klassenpfad zu ändern.
    Ersetzen Sie:
    ```properties
    flyway.locations=filesystem:/migrations
    ```
    durch:
    ```properties
    spring.flyway.locations=classpath:db/migration
    ```

3.  **JPA/Hibernate-Konfiguration:**
    Da Flyway für die Verwaltung des Datenbankschemas verwendet wird, wird empfohlen, `spring.jpa.hibernate.ddl-auto` auf `validate` oder `none` zu setzen.
    ```properties
    spring.jpa.hibernate.ddl-auto=validate
    ```

## Anwendung starten

1.  **Bauen Sie das Projekt mit Maven:**
    ```bash
    mvn clean install
    ```

2.  **Führen Sie die Anwendung aus:**
    ```bash
    java -jar target/Aggregator-0.0.1-SNAPSHOT.jar
    ```
    Die Anwendung wird unter `http://localhost:8088` gestartet.

## API-Endpunkte

Die API-Dokumentation ist nach dem Start der Anwendung unter [http://localhost:8088/swagger-ui.html](http://localhost:8088/swagger-ui.html) verfügbar.

### Hauptendpunkte

-   `GET /search/q`: Sucht nach Geschäften basierend auf Artikel- und Standortkriterien.
    -   **Query-Parameter:**
        -   `title` (String): Artikeltitel.
        -   `size` (String, optional): Artikelgröße.
        -   `priceFrom` (BigDecimal, optional): Mindestpreis.
        -   `priceTo` (BigDecimal, optional): Höchstpreis.
        -   `color` (String, optional): Artikelfarbe.
        -   `radius` (Integer, optional): Suchradius in Kilometern.
        -   `sex` (Integer, optional): Geschlecht (z.B. 1 für männlich, 2 für weiblich).
        -   `userLat` (BigDecimal, optional): Breitengrad des Benutzers.
        -   `userLon` (BigDecimal, optional): Längengrad des Benutzers.

-   `GET /shops/{shopId}/items`: Ruft Artikel für ein bestimmtes Geschäft ab.
    -   **Path-Parameter:**
        -   `shopId` (Integer): ID des Geschäfts.
    -   **Query-Parameter:**
        -   `title` (String, optional): Artikeltitel.
        -   `size` (String, optional): Artikelgröße.
        -   `color` (String, optional): Artikelfarbe.
        -   `sex` (Integer, optional): Geschlecht.
        -   `userLat` (BigDecimal, optional): Breitengrad des Benutzers.
        -   `userLon` (BigDecimal, optional): Längengrad des Benutzers.
        -   `radius` (Integer, optional): Suchradius in Kilometern.
        -   `page` (int, optional, default=0): Seitennummer.
        -   `sizePerPage` (int, optional, default=10): Anzahl der Artikel pro Seite.

## Datenbank

Die Anwendung verwendet Flyway zur Verwaltung von Datenbankschema-Migrationen. Die Migrationsskripte befinden sich im Verzeichnis `src/main/resources/db/migration`.

-   `V2__INSERT_BRANCH_TABLE.sql`: Fügt Beispieldaten in die `branch`-Tabelle ein.

## Datenimport

Das Projekt enthält mehrere Klassen zum Importieren von Daten aus JSON-Dateien in die PostgreSQL-Datenbank:
- `JsonToPostgresImporter.java`
- `JsonToPostgresJDMale.java`
- `JsonToPostgresNikeFemale.java`
- `JsonToPostgresNikeMale.java`

Diese Klassen sind nicht direkt über die API zugänglich und müssen manuell ausgeführt oder in einen geeigneten Service integriert werden, um die Daten zu importieren. Die JSON-Dateien befinden sich in `src/main/resources`.
