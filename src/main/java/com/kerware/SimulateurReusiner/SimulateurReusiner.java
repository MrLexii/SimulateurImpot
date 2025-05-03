package com.kerware.SimulateurReusiner;

import com.kerware.simulateur.SituationFamiliale;

/**
 * Simule le calcul de l'impôt sur le revenu en France pour l'année 2024 sur les revenus de 2023.
 * Prend en compte les revenus du foyer, la situation familiale, les enfants à charge,
 * les enfants handicapés, le statut de parent isolé, et les règles fiscales applicables.
 *
 * <p><b>Exigences métier couvertes :</b></p>
 * <ul>
 *   <li>EM-000 : Validation des règles d’entrée (revenus, situation, enfants, cohérence)</li>
 *   <li>EM-001 : Calcul de l’abattement de 10% (revenus imposables)</li>
 *   <li>EM-002 : Calcul du nombre de parts fiscales (situation, enfants, handicaps, parent isolé)</li>
 *   <li>EM-003 : Calcul de l’impôt avec quotient familial et plafonnement</li>
 *   <li>EM-004 : Application de la contribution exceptionnelle sur hauts revenus (CEHR)</li>
 *   <li>EM-005 : Application de la décote selon seuils</li>
 * </ul>
 *
 * <p>Limitation : le simulateur prend en charge jusqu’à 7 enfants maximum.</p>
 */
public class SimulateurReusiner {

    // Modules de calcul
    private CalculAbattement calculAbattement;
    private CalculPartsFiscales calculPartsFiscales;
    private CalculContributionExceptionnelle calculContributionExceptionnelle;
    private CalculImpots calculImpots;
    private CalculPlafondQuotientFamille calculPlafondQuotientFamille;
    private Decote decote;

    // Données d’entrée
    private int revenu1;
    private int revenu2;
    private int nbEnfants;
    private int nbEnfantsHandicap;
    private boolean parentIsole;
    private SituationFamiliale situation;

    // Données intermédiaires
    private double revenuFiscalReference;
    private double abattement;
    private double nbPartsDeclarant;
    private double nbPartsFoyerFiscal;
    private double variableDecote;
    private double impotFoyerFiscal;
    private double impotFoyerFiscalAvantDecote;
    private double contributionExceptionnelle;

    // Getters pour tests unitaires et interfaçage legacy
    public double getRevenuReference() { return revenuFiscalReference; }
    public double getDecote() { return variableDecote; }
    public double getAbattement() { return abattement; }
    public double getNbPartsFoyerFiscal() { return nbPartsFoyerFiscal; }
    public double getImpotAvantDecote() { return impotFoyerFiscalAvantDecote; }
    public double getImpotNet() { return impotFoyerFiscal; }
    public int getRevenuNetDeclatant1() { return revenu1; }
    public int getRevenuNetDeclatant2() { return revenu2; }
    public double getContributionExceptionnelle() { return contributionExceptionnelle; }

    /**
     * Initialise les modules nécessaires pour le calcul complet d'impôt.
     */
    public SimulateurReusiner() {
        this.calculAbattement = new CalculAbattement();
        this.calculPartsFiscales = new CalculPartsFiscales();
        this.calculContributionExceptionnelle = new CalculContributionExceptionnelle();
        this.calculImpots = new CalculImpots();
        this.calculPlafondQuotientFamille = new CalculPlafondQuotientFamille();
        this.decote = new Decote();
    }

    /**
     * Point d'entrée du simulateur — calcule l'impôt net à partir des données fournies.
     *
     * @param rev1 Revenu du déclarant principal
     * @param rev2 Revenu du second déclarant (si couple)
     * @param sit Situation familiale
     * @param enfants Nombre d’enfants à charge
     * @param enfantsHandicapes Nombre d’enfants handicapés
     * @param isole Vrai si parent isolé
     * @return Montant de l’impôt net à payer (arrondi à l’entier)
     */
    public int calculerImpot(int rev1, int rev2, SituationFamiliale sit, int enfants, int enfantsHandicapes, boolean isole) {
        System.out.println("=========== DÉBUT DU CALCUL DE L'IMPÔT ===========");
        afficherEntrees(rev1, rev2, sit, enfants, enfantsHandicapes, isole);

        // Validation des données d'entrée
        validerEntrees(rev1, rev2, sit, enfants, enfantsHandicapes, isole);

        // Initialisation des données internes
        initialiserDonnees(rev1, rev2, sit, enfants, enfantsHandicapes, isole);

        // Étape 1 : Calcul de l'abattement
        calculAbattement.calculerAbattement(revenu1, revenu2, situation);
        abattement = calculAbattement.getAbattement();
        revenuFiscalReference = calculAbattement.getRevenuFiscalRef();
        afficherResultatAbattement();

        // Étape 2 : Calcul du nombre de parts fiscales
        calculPartsFiscales.calculerParts(nbEnfants, situation, parentIsole, nbEnfantsHandicap);
        nbPartsDeclarant = calculPartsFiscales.getPartsDeclarant();
        nbPartsFoyerFiscal = calculPartsFiscales.getPartsFoyerFiscal();
        afficherPartsFiscales();

        // Étape 3 : Calcul de la contribution exceptionnelle sur les hauts revenus
        calculContributionExceptionnelle.calculerContribution(revenuFiscalReference, nbPartsDeclarant);
        contributionExceptionnelle = calculContributionExceptionnelle.getContributionExceptionnelle();
        afficherContributionExceptionnelle();

        // Étape 4 : Calcul de l'impôt avant la décote et le plafonnement
        impotFoyerFiscalAvantDecote = calculImpots.calculImpôtAvantPlafond(revenuFiscalReference, nbPartsDeclarant);
        impotFoyerFiscal = calculImpots.calculImpôtFoyer(revenuFiscalReference, nbPartsFoyerFiscal);
        plafonnerImpotFoyerFiscal();

        // Étape 5 : Appliquer la décote
        impotFoyerFiscal = decote.calculDecote(impotFoyerFiscal, nbPartsDeclarant, contributionExceptionnelle);
        variableDecote = decote.getValeur();
        afficherResultatFinal();

        System.out.println("=========== FIN DU CALCUL ===========\n");
        return (int) impotFoyerFiscal;
    }

    /**
     * Affiche les entrées pour le calcul de l'impôt.
     *
     * @param rev1 Revenu du déclarant principal
     * @param rev2 Revenu du second déclarant (si applicable)
     * @param sit Situation familiale
     * @param enfants Nombre d'enfants à charge
     * @param enfantsHandicapes Nombre d'enfants handicapés
     * @param isole Vrai si parent isolé
     */
    private void afficherEntrees(int rev1, int rev2, SituationFamiliale sit, int enfants, int enfantsHandicapes, boolean isole) {
        System.out.printf("Entrées : Revenu1 = %d €, Revenu2 = %d €, Situation = %s, Enfants = %d, Enfants handicapés = %d, Parent isolé = %s%n", rev1, rev2, sit, enfants, enfantsHandicapes, isole);
    }

    /**
     * Affiche le résultat de l'abattement et du revenu fiscal de référence.
     */
    private void afficherResultatAbattement() {
        System.out.printf("→ Abattement total : %.2f €%n", abattement);
        System.out.printf("→ Revenu fiscal de référence : %.2f €%n", revenuFiscalReference);
    }

    /**
     * Affiche les informations sur les parts fiscales.
     */
    private void afficherPartsFiscales() {
        System.out.printf("→ Parts déclarants : %.2f%n", nbPartsDeclarant);
        System.out.printf("→ Parts fiscales (totales) : %.2f%n", nbPartsFoyerFiscal);
    }

    /**
     * Affiche la contribution exceptionnelle sur les hauts revenus.
     */
    private void afficherContributionExceptionnelle() {
        System.out.printf("→ Contribution exceptionnelle (CEHR) : %.2f €%n", contributionExceptionnelle);
    }

    /**
     * Plafonne l'impôt en fonction du quotient familial.
     */
    private void plafonnerImpotFoyerFiscal() {
        impotFoyerFiscal = calculPlafondQuotientFamille.calculPlafonnementQuotientFamilial(
            impotFoyerFiscalAvantDecote, impotFoyerFiscal, nbPartsDeclarant, nbPartsFoyerFiscal
        );
        System.out.printf("→ Baisse d’impôt (plafonnement) : %.2f €%n", calculPlafondQuotientFamille.getBaisseImpot());
        System.out.printf("→ Plafond appliqué : %.2f €%n", calculPlafondQuotientFamille.getPlafond());
        System.out.printf("→ Impôt après plafonnement : %.2f €%n", impotFoyerFiscal);
    }

    /**
     * Affiche le résultat final de l'impôt après la décote.
     */
    private void afficherResultatFinal() {
        System.out.printf("→ Décote appliquée : %.2f €%n", variableDecote);
        System.out.printf("→ Impôt net à payer (avec CEHR et décote) : %.2f €%n", impotFoyerFiscal);
    }

    /**
     * Valide les entrées utilisateur avant tout calcul.
     * 
     * @param rev1 Revenu déclarant 1
     * @param rev2 Revenu déclarant 2
     * @param sit Situation familiale
     * @param enfants Nombre d’enfants
     * @param enfantsHandicapes Nombre d’enfants handicapés
     * @param isole Parent isolé ?
     * @throws IllegalArgumentException si données incohérentes
     */
    private void validerEntrees(int rev1, int rev2, SituationFamiliale sit, int enfants, int enfantsHandicapes, boolean isole) {
        if (rev1 < 0 || rev2 < 0) {
            throw new IllegalArgumentException("Les revenus ne peuvent pas être négatifs.");
        }
        if (sit == null) {
            throw new IllegalArgumentException("La situation familiale est obligatoire.");
        }
        if (enfants < 0 || enfantsHandicapes < 0) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut être négatif.");
        }
        if (enfantsHandicapes > enfants) {
            throw new IllegalArgumentException("Les enfants handicapés ne peuvent excéder le nombre total d'enfants.");
        }
        if (enfants > 7) {
            throw new IllegalArgumentException("Limite maximale : 7 enfants pris en compte.");
        }
        if ((sit == SituationFamiliale.MARIE || sit == SituationFamiliale.PACSE) && isole) {
            throw new IllegalArgumentException("Un parent isolé ne peut être marié ou pacsé.");
        }
        if ((sit == SituationFamiliale.CELIBATAIRE || sit == SituationFamiliale.DIVORCE || sit == SituationFamiliale.VEUF) && rev2 > 0) {
            throw new IllegalArgumentException("Un contribuable seul ne peut avoir deux revenus.");
        }
    }

    /**
     * Initialise les données d’entrée dans les attributs internes.
     *
     * @param rev1 Revenu 1
     * @param rev2 Revenu 2
     * @param sit Situation familiale
     * @param enfants Nombre d’enfants
     * @param enfantsHandicapes Nombre d’enfants handicapés
     * @param isole Parent isolé
     */
    private void initialiserDonnees(int rev1, int rev2, SituationFamiliale sit, int enfants, int enfantsHandicapes, boolean isole) {
        this.revenu1 = rev1;
        this.revenu2 = rev2;
        this.nbEnfants = enfants;
        this.nbEnfantsHandicap = enfantsHandicapes;
        this.parentIsole = isole;
        this.situation = sit;
    }
}
