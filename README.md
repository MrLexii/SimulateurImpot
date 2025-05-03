---------------------------------------------------------------
                   Simulateur Reusiner
                   (Simulateur d'impôt sur le revenu)
---------------------------------------------------------------

Description du projet :
-----------------------
Le projet Simulateur Reusiner est un simulateur d'impôt sur le revenu français.
Il permet de calculer le montant de l'impôt dû pour un foyer fiscal en fonction de divers critères,
notamment la situation familiale, le nombre d'enfants, les revenus, et les règles fiscales appliquées en France.
Le simulateur prend en compte la décote, le quotient familial, la contribution exceptionnelle sur les hauts revenus (CEHR), les abattements, et les tranches d'imposition progressives pour déterminer l'impôt net.

Les principales règles fiscales incluses dans ce simulateur sont :
- La gestion des parts fiscales selon la situation familiale et les enfants.
- Le plafonnement du quotient familial.
- Le calcul de la décote et de la contribution exceptionnelle sur les hauts revenus (CEHR).
- Le calcul des impôts à partir des tranches d’imposition et des taux applicables.

---------------------------------------------------------------
Structure du projet :
----------------------

1. **CalculPartsFiscales.java**
   - Cette classe calcule le nombre de parts fiscales d'un foyer fiscal en fonction de la situation familiale, du nombre d'enfants à charge, des enfants handicapés, ainsi que des règles spécifiques pour les parents isolés ou veufs.

2. **CalculPlafondQuotientFamille.java**
   - Applique le plafonnement du quotient familial, ce qui limite l'avantage fiscal obtenu grâce aux parts supplémentaires (enfants, parent isolé, etc.).

3. **Constante.java**
   - Centralise toutes les constantes fiscales utilisées dans les calculs, telles que les tranches d'imposition, les taux associés, les seuils de décote, les plafonds du quotient familial et les taux de la Contribution Exceptionnelle sur les Hauts Revenus (CEHR).

4. **Decote.java**
   - Calcule la décote applicable à l'impôt sur le revenu lorsque celui-ci est inférieur à un certain seuil. La décote permet de réduire ou annuler l’impôt pour les foyers à faibles revenus.

5. **SimulateurImpots.java**
   - Point d'entrée du simulateur d'impôt. Cette classe permet d'initier le calcul des impôts pour un foyer, de l'application des tranches d'imposition jusqu'au calcul final de l'impôt après quotient familial, décote et CEHR, c'est la classe de centralisation de tout le simulateur.

6. **CalculImpot.java**
   - Calcule l'impôt brut du foyer fiscal en fonction du revenu imposable, des tranches d'imposition et des parts fiscales. Cette classe applique les tranches et les taux progressifs pour déterminer l'impôt brut avant le calcul de la décote et de la CEHR.

7. **CalculContributionExceptionnelle.java**
   - Calcule la Contribution Exceptionnelle sur les Hauts Revenus (CEHR) en fonction d'un revenu de référence du foyer fiscal et du nombre de parts par déclarant (sans compter les enfants), en utilisant les tranches et les taux applicables.

8. **TestSimulateurReusiner.java**
    - Classe de test permettant de valider le bon fonctionnement du simulateur en vérifiant les résultats pour différents scénarios de foyers fiscaux. Cette classe permet de s'assurer que les calculs d'impôt respectent les règles fiscales françaises et que les différents scénarios sont correctement traités.
  
9. **AdaptateurSimuReus.java**
    - Classe permettant d'adapter notre simulateur pour le faire fonctionner avec nos tests.
    - Adaptateur permettant d'utiliser le simulateur {@link SimulateurReusiner} avec l'interface standard {@link ICalculateurImpot}.

10. **CalculAbattement.java**
    - Cette classe calcule l'abattement de 10% appliqué aux revenus nets des déclarants, conformément aux règles fiscales en vigueur en fonction des deux revenus et de la situation familiale.

---------------------------------------------------------------
Utilisation :
-------------
1. **Constante** :
   - Étant la classe avec toutes les constantes utilisées sur tout le projet, il est facile de mettre à jour ou de juste changer les valeurs.

2. **TestSimulateur** :
   - Utiliser cette classe pour tester le simulateur d'impôt dans différents scénarios et s'assurer du bon fonctionnement des calculs en changeant ou ajoutant des tests JUnit.

3. **Lancement** :
   - Comme tout programme Java, il faut appuyer sur la flèche verte pour lancer le programme ^^.

---------------------------------------------------------------
Exigences du projet :
----------------------
- Soit Lisible avec des concepts métier
- Soit commenté de manière équilibrée
- Ne montre pas de "nombre magiques" mais des constantes ou paramètres soigneusement nommés
- Ne montre pas de classes avec trop de responsabilités
- Ne montre pas de fonction trop longues
- Soit modulaire et paramétrable pour le calcul des impôts 2025
- 100% des tests unitaire fonctionnels passent
- Tests unitaires fonctionnels couvrent au moins 90% des lignes de code du simulateur réusiné
- Le code passe le contrôle par analyse du code avec CheckStyle avec le fichier de règle fourni

---------------------------------------------------------------
Comment contribuer :
-------------------
- Cloner le projet depuis le dépôt Git.
- Ajouter ou modifier des fonctionnalités en fonction des mises à jour fiscales.
- Tester les nouvelles fonctionnalités et soumettre des pull requests avec des tests unitaires appropriés.
- Assurer la compatibilité avec les versions fiscales futures et maintenir le code à jour.

---------------------------------------------------------------
Licences et informations supplémentaires :
-----------------------------------------
Ce projet est distribué sous la licence F.PINSON & Z.PANASSIE.

---------------------------------------------------------------
