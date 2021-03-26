Présentation du projet
Vous allez devoir, par groupe de 3, développer un serveur web.
Critères d'évaluation
- [x] il doit être publié sur github
  -   [ ] il doit posséder un readme qui explique brièvement ce que fait le projet et comment l'utiliser
- [x] lancer le projet doit se résumer à un .bat, un .sh ou quelques commandes documentées
- [x] le serveur doit être implémenté en Java (ou en C/C++)
- [x] il ne doit pas utiliser de librairie « toutes faites »
  -   [x] pas de jar en dépendance (en dehors de la jre bien sûr) => « from scratch »
  -   [x] mais peut utiliser quelques algorithmes tout faits comme base64/md5
- [x] il doit savoir exploiter une requête GET en HTTP/1.x (en ignorant les entêtes non gérées)
  -   [x] découpage de la requête « à la main » (parsing) et traitement
- [x] le serveur doit être fonctionnel (i.e. afficher correctement les sites exemples fournis)
- [x] le serveur doit gérer proprement les erreurs les plus courantes (400, 404)
- [x] il doit gérer plusieurs connexions en parallèle (en étant par exemple multi-tâche avec un
thread = une connexion)
- [ ] il doit être « bien écrit » : code documenté, maintenable, évolutif
- [x] il doit afficher du log sur stdout : ip de l'appelant + requête
- [x] il doit gérer le multisite (i.e. pouvoir héberger plusieurs domaines)
- [ ] il doit pouvoir protéger une ressource par une authentification basique
  -   [x] un répertoire est protégé s'il contient un fichier .htpasswd
  -   [ ] le fichier contient des lignes sous la forme « username:password_en_md5 »
- [ ] il faut externaliser la configuration dans un fichier properties
  -   [ ] répertoire racine où mettre les sites web, port tcp (80 par défaut)
	
Il doit implémenter une fonctionnalité « bonus » au choix :
 - [ ] gérer les server-side includes (#include et #exec)
 - [ ] générer du contenu dynamique à l'aide d'un programme externe (php, python, node)
 - [ ] gérer le listing des répertoires (doit pouvoir être désactivé par configuration)
 - [ ] compresser les ressources js et css en gzip
Bien écrit
Le code doit être documenté : les commentaires doivent être réguliers et pertinent, ne
pas expliquer ce qui est fait mais pourquoi c'est fait.
Le code doit être maintenable : n'importe qui doit pouvoir intervenir facilement pour
corriger le code au besoin. Le code doit être compréhensible.