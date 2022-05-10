- scruter un dossier particulier, à la recherche des fichiers users_<YYYYMMDDHHmmSS>.csv

- parser ces fichiers qui sont au format :
  <Numero_Securite_Sociale>, <Nom>, <Prenom>, <Date_Naissance>, <Numero_Telephone>, <E_Mail>, <ID_Remboursement>, <Code_Soin>, <Montant_Remboursement>

- peuple une base de données relationnelle avec ces entrées :
    - l'ID remboursement est un identifiant qui permet de déterminer s'il s'agit d'un insert ou d'un update
    - une colonne <Timestamp_fichier> est extraite du nom du fichier

- une fois traités les fichiers sont déplacés dans un autre dossier

- les lignes en erreur (format incorrect) sont déplacés dans un dossier de rejet, dans un fichier avec le timestamp du fichier d'origine

- optionnel : les fichiers users_<YYYYMMDDHHmmSS>.json peuvent également être supportés.