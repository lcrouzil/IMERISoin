# Team 03
## Web Services
Le serveur est hébérgé sur uvicorn en utilisant l'adresse ip de l'ordinateur de celui qui gère le Web Services. Le port utilisé est le 8000. Toutes les URI dont le superviseur(Java) ainsi que le robot(Arduino) a besoin sont implémentés pour renvoyer les informations nécessaires. Dans le cas où la requête n'a pas pu aboutir pour différentes raisons: problème de connexion, ajout dans la database de quelque chose qui existe déjà, mauvais arguments donnés dans la requête... Le service web renvoie un code correspondant à l'erreur survenue.
