# Projet Final Java :

## Gestionnaire d'insertion de fichier local vers une base de données (Mysql)

### 1 - Installation:

Récupérer le projet via la commande :

`git clone https://github.com/MaximeRCD/JavaFinalProj.git`

### 2 - Lancement du projet:

Il y a un fichier db_settings.properties qu'il faut modifier selon l'environnement depuis lequel on accède à la base de données.

Il est composé de 5 champs :
- environment : < environement qui héberge la bdd >.
- server : localhost:3306
- db_name : users_db
- user_name : root
- password : root

Il suffit de préciser l'ensemble des ces cinqs paramètres pour pouvoir lancé le code correctement.


Lancer la classe main en laissant commenté la partie qui l'est. Elle sert à replacer automatiquement les fichiers traités par une exécution précédente dans le dossier à traité.

### 3 - Retour d'expérience:
#### 3.0 - Sujet
Le but du projet est de développer un programme batch java qui devra implémenter les fonctionnalités suivantes:

- scruter un dossier particulier, à la recherche des fichiers users_<YYYYMMDDHHmmSS>.csv

- parser ces fichiers qui sont au format :
  <Numero_Securite_Sociale>, <Nom>, <Prenom>, <Date_Naissance>, <Numero_Telephone>, <E_Mail>, <ID_Remboursement>, <Code_Soin>, <Montant_Remboursement>

- peuple une base de données relationnelle avec ces entrées :
    - l'ID remboursement est un identifiant qui permet de déterminer s'il s'agit d'un insert ou d'un update
    - une colonne <Timestamp_fichier> est extraite du nom du fichier

- une fois traité les fichiers sont déplacés dans un autre dossier

- les lignes en erreur (format incorrect) sont déplacés dans un dossier de rejet, dans un fichier avec le timestamp du fichier d'origine

- optionnel : les fichiers users_<YYYYMMDDHHmmSS>.json peuvent également être supportés.

#### 3.0.1 - Stack technique

Le projet doit être fait en Java, avec Maven pour le build.
Des tests unitaires peuvent (doivent) être implémentés avec des Junit.
Des dépendances peuvent (doivent) être inclues, ne serait-ce que le driver JDBC pour la base (PostgreSQL recommandée).


#### 3.0.2 - Livrables

Les livrables attendus via Mootse ou WeTransfer sont :

- un zip avec le code source du programme : note sur 14

- un document technique rédigé expliquant les choix d'implémentation (un readme sera OK) : note sur 6

#### 3.1 - Choix d'implémentation

#### 3.1.1 Gestion des accès concurentiel aux fichiers.

Lors du developpement de l'insertion des lignes d'un fichier dans la base. Nous nous sommes heurté au problème d'accès concurentiel par deux thread différents sur la JVM lorsque nous voulions déplacer le fichier traité dans le folder correspondant. 
Un fix est d'intérrompre le thread de lecture du fichier pour lancer le thread de déplacement.
Une conséquence faisant suite à cette difficulté à été de choisir d'avoir un projet fonctionnel le plus rapidement possible et donc de traiter les fichiers dans leur globalité, c'est à dire que si l'insertion d'une ligne renvoie une erreur, l'ensemble du fichier d'origine est déplacé dans le folder des fichiers problématiques.
Si ce n'est pas la première ligne alors on risque de d'insérer plusieurs fois des lignes dans la base mais cela est gérer par la faculter à update des données si l'id_remboursement est le même.
Cela nous permettait de limiter les ouvertures et fermetures de flux vers le fichier à traiter.

#### 3.1.2 Gestion des propriétes de connexion.

Nous avons choisi d'inscrire les propriétes de connexion à la base de données car nous ne travaillions pas depuis le même environnement.
Elyes via WAMP et Maxime via docker, qui ne requièrent pas les mêmes propriétés. 
Ainsi, en ajoutant le fichier .properties au .gitignore, chacun pouvait récupérer le code avec ses propres identifiants de connexion et travailler sur des environnements différents.


#### 3.2 - Améliorations à apporter

Gérer l'ingestion par ligne et non par fichier.

Ajouter des paramètres en ligne de commande à l'objet main du type : 
--ingest_data : pour lancer la partie du code qui ingère les données.
--input-data_folder <folder_path>
--problematic_data_folder <folder_path>
--computed_data_folder <folder_path>
pour retirer l'ensemble des chemins encore en dur dans le code et ainsi pouvoir le lancer sur n'importe quel environnement.

--put_data_back : pour exécuter la seconde partie (commenté) du main qui s'occupe de remettre les fichiers déplacés par une exécution précédente dans le dossier input_data.