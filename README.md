# Merkel Tree

This project aims to generate Merkel trees, and more broadly adds a CRUD around this object.

[![Build](https://github.com/alainncls/merkel-tree/actions/workflows/pipeline.yml/badge.svg)](https://github.com/alainncls/merkel-tree/actions/workflows/pipeline.yml)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=alainncls_merkel-tree&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=alainncls_merkel-tree)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=alainncls_merkel-tree&metric=coverage)](https://sonarcloud.io/summary/new_code?id=alainncls_merkel-tree)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=alainncls_merkel-tree&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=alainncls_merkel-tree)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=alainncls_merkel-tree&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=alainncls_merkel-tree)

## Launch (in an IDE)

1. Clone this repository

   ```
   git clone
   ```

2. Start a MongoDB instance

   ```
   docker run -p 27017:27017 mongo
   ```

3. Launch `MerkelTreeApplication`

## Generate documentation

One single command:

```
   ./mvn package
```

The HTML documentation will be generated in `target/generated-docs` and the snippets in `target/generated-snippets`.

## Launch (from the JAR)

1. Generate the JAR:

   ```
      ./mvn package
   ```

2. Start the JAR:

   ```
      java -jar target/merkel-tree-0.0.1-SNAPSHOT.jar
   ```

3. The application is available on port 8080 and the documentation is located at http://localhost:8080/docs/index.html.