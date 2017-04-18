# testpe
## Construction du projet

Récupérer le projet et mettre à jour le fichier config.properties avec :
- Le fichier à lire
- Le fichier résultat

Eventuellement ajuster les informations dans le dictionnary.properties

Mettre à jour le logback.xml si besoin

Faire un *mvn clean install*

Lancer le jar, la main classe est com.poleemploi.javanais

*java -cp {emplacementdu jar}/javanais-1.0.0-0-jar-with-dependencies.jar com.poleemploi.Javanais*

# hypothèse
- un mot qui commence par une voyelle en début de ligne fait partie du traitement
- les accents sont des voyelles : é è ê à ô ù


## RAF
- Faire les TU + poussés
- Faire l'implémentation de l'algo pour la transformation inverse avec gestion des mots corrects (verbe avoir ....)
- Tester l'encodage des charactères en UTF-8
- Faire un assembly
- Faire attention à la complexité des méthodes
- gérer la dernière ligne dans le fichier résultat