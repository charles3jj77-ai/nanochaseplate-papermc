# Nano Chaseplate — Plugin Paper 1.26.2

## Ce que fait le plugin

- Ajoute un item **Nano Chaseplate** (basé sur une Élytre, avec nom/lore
  colorés, un `CustomModelData` et un effet brillant).
- Accorde le **vol illimité** (comme en créatif) tant qu'il est équipé
  dans le slot plastron, y compris en survie/aventure.
- **Ne s'use jamais** (`Unbreakable`).
- **Impossible à fabriquer** : aucune recette n'est enregistrée, et le
  plugin bloque en plus toute tentative de le faire apparaître via une
  table de craft ou de le dupliquer via une enclume.
- **Uniquement obtenable via `/nanochase`**, commande réservée aux
  opérateurs (`permission: nanochaseplate.give`, `default: op`).
  - `/nanochase` → se donne l'item à soi-même (si OP).
  - `/nanochase <joueur>` → donne l'item à un autre joueur (si OP).

## Compiler le .jar

Il vous faut **Java 21** et **Maven** installés sur votre machine
(avec un accès internet, pour télécharger l'API Paper la première fois).

```bash
cd nano-chaseplate
mvn package
```

Le fichier prêt à l'emploi apparaîtra dans :

```
target/NanoChaseplate.jar
```

Copiez-le simplement dans le dossier `plugins/` de votre serveur Paper
1.26.2, puis redémarrez (ou `/reload confirm`, non recommandé) le serveur.

## Si "1.26.2" n'est pas encore disponible sur le dépôt Maven de PaperMC

Ouvrez `pom.xml` et changez la version de la dépendance `paper-api`
pour la faire correspondre exactement à la version affichée par votre
serveur (`/version`), par exemple `1.21.4-R0.1-SNAPSHOT`. Le code du
plugin n'utilise que des API stables présentes depuis plusieurs
versions, donc il compilera sans autre changement.

## Personnaliser le design (vraie texture custom)

Le `CustomModelData` utilisé est `20260702` (défini dans
`NanoChaseItem.java`, constante `CUSTOM_MODEL_DATA`). Pour lui donner
une véritable texture personnalisée (pas seulement un nom/lore/glow),
créez un resource pack avec un modèle JSON pour l'élytre pointant sur
ce `custom_model_data`, et distribuez-le à vos joueurs (ou configurez
`resource-pack` dans `server.properties`).
