Présentation du projet
Vous allez devoir, par groupe de 3, développer un serveur web.
Critères d'évaluation
- [x] il doit être publié sur github
  -   [x] il doit posséder un readme qui explique brièvement ce que fait le projet et comment l'utiliser
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
- [x] il doit être « bien écrit » : code documenté, maintenable, évolutif
- [x] il doit afficher du log sur stdout : ip de l'appelant + requête
- [x] il doit gérer le multisite (i.e. pouvoir héberger plusieurs domaines)
- [x] il doit pouvoir protéger une ressource par une authentification basique
  -   [x] un répertoire est protégé s'il contient un fichier .htpasswd
  -   [x] le fichier contient des lignes sous la forme « username:password_en_md5 »
- [x] il faut externaliser la configuration dans un fichier properties
  -   [x] répertoire racine où mettre les sites web, port tcp (80 par défaut)
	
Il doit implémenter une fonctionnalité « bonus » au choix :
 - [ ] gérer les server-side includes (#include et #exec)
 - [ ] générer du contenu dynamique à l'aide d'un programme externe (php, python, node)
 - [x] gérer le listing des répertoires (doit pouvoir être désactivé par configuration)
 - [ ] compresser les ressources js et css en gzip
Bien écrit
Le code doit être documenté : les commentaires doivent être réguliers et pertinent, ne
pas expliquer ce qui est fait mais pourquoi c'est fait.
Le code doit être maintenable : n'importe qui doit pouvoir intervenir facilement pour
corriger le code au besoin. Le code doit être compréhensible.
   
# Projet

Le projet permet d'afficher les sites web verti/dopetrope/test en créeant les pages
et les requêtes envoyé par l'utilisateur

## Utilisation

Pour utiliser ce projet vous devez modifier votre fichier etc/hosts

Pour cela vous devez écrire dedans ce qui suit en remplaçant IP_PC par votre IP à vous :
- IP_PC www.verti.miage
- IP_PC www.dopetrope.miage
- IP_PC www.test.miage

Vous devez aussi avoir un fichier config.properties contenant les infos suivantes :

- ipV4=IP_PC
- port=80
- pathToSites= Lien vers le fichier contenant les sites le fichier est fourni il faut pointer vers sites
- acces_dir= écrire true ou false si vous voulez le listing des répertoires

Pour lancer le projet en local il faut aller dans bindist/bin et dedans vous avez un fichier main et main.bat. 

Nous n'avons pas pu tester si le main marche sur Mac ou Linux par contre main.bat fonctionne sous Windows.

Le fichier config.properties doit être au même niveau que votre main, et vous devez le lancer depuis le répertoire bin.
