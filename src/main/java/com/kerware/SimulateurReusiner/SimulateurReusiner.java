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
        System.out.printf("Entrées : Revenu1 = %d €, Revenu2 = %d €, Situation = %s, Enfants = %d, Enfants handicapés = %d, Parent isolé = %s%n",
                rev1, rev2, sit, enfants, enfantsHandicapes, isole);

        // ----- Étape 1 : Vérification des entrées -----
        // EM-000 : Validation des règles métier
        validations(rev1, rev2, sit, enfants, enfantsHandicapes, isole);
        
        // ----- Étape 2 : Initialisation des données internes -----
        initialisation(rev1, rev2, sit, enfants, enfantsHandicapes, isole);

        // ----- Étape 3 : Calcul de l’abattement de 10% -----
        // EM-001 : Calcul de l’abattement de 10%
        calculAbattement.calculerAbattement(revenu1, revenu2, situation);
        this.abattement = calculAbattement.getAbattement();
        this.revenuFiscalReference = calculAbattement.getRevenuFiscalRef();

        System.out.printf("→ Abattement total : %.2f €%n", abattement);
        System.out.printf("→ Revenu fiscal de référence : %.2f €%n", revenuFiscalReference);

        // ----- Étape 4 : Calcul du nombre de parts fiscales -----
        // EM-002 : Calcul des parts fiscales
        calculPartsFiscales.calculerParts(nbEnfants, situation, parentIsole, nbEnfantsHandicap);
        this.nbPartsDeclarant = calculPartsFiscales.getPartsDeclarant();
        this.nbPartsFoyerFiscal = calculPartsFiscales.getPartsFoyerFiscal();

        System.out.printf("→ Parts déclarants : %.2f%n", nbPartsDeclarant);
        System.out.printf("→ Parts fiscales (totales) : %.2f%n", nbPartsFoyerFiscal);

        // ----- Étape 5 : Calcul de la CEHR -----
        // EM-004 : Contribution exceptionnelle sur hauts revenus
        calculContributionExceptionnelle.calculerContribution(revenuFiscalReference, nbPartsDeclarant);
        this.contributionExceptionnelle = calculContributionExceptionnelle.getContributionExceptionnelle();

        System.out.printf("→ Contribution exceptionnelle (CEHR) : %.2f €%n", contributionExceptionnelle);

        // ----- Étape 6 : Impôt sans quotient familial -----
        // EM-003 : Calcul de l’impôt sans quotient familial (référence)
        double impDec1 = calculImpots.calculImpôtAvantPlafond(revenuFiscalReference, nbPartsDeclarant);
        System.out.printf("→ Impôt sans quotient familial : %.2f €%n", impDec1);

        // ----- Étape 7 : Impôt avec quotient familial -----
        // EM-003 : Calcul de l’impôt avec quotient familial
        this.impotFoyerFiscal = calculImpots.calculImpôtFoyer(revenuFiscalReference, nbPartsFoyerFiscal);
        System.out.printf("→ Impôt avec quotient familial : %.2f €%n", this.impotFoyerFiscal);

        // ----- Étape 8 : Plafonnement du quotient familial -----
        // EM-003 : Application du plafonnement du quotient familial
        this.impotFoyerFiscal = calculPlafondQuotientFamille.calculPlafonnementQuotientFamilial(
                impDec1, this.impotFoyerFiscal, nbPartsDeclarant, nbPartsFoyerFiscal);

        System.out.printf("→ Baisse d’impôt (plafonnement) : %.2f €%n", calculPlafondQuotientFamille.getBaisseImpot());
        System.out.printf("→ Plafond appliqué : %.2f €%n", calculPlafondQuotientFamille.getPlafond());
        System.out.printf("→ Impôt après plafonnement : %.2f €%n", this.impotFoyerFiscal);

        // ----- Étape 9 : Application de la décote -----
        // EM-005 : Application de la décote
        this.impotFoyerFiscal = decote.calculDecote(this.impotFoyerFiscal, nbPartsDeclarant, contributionExceptionnelle);
        this.variableDecote = decote.getValeur();

        System.out.printf("→ Décote appliquée : %.2f €%n", variableDecote);
        System.out.printf("→ Impôt net à payer (avec CEHR et décote) : %.2f €%n", this.impotFoyerFiscal);

        System.out.println("=========== FIN DU CALCUL ===========\n");

        // ----- Étape 10 : Retour de l’impôt net -----
        return (int) this.impotFoyerFiscal;
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
    private void validations(int rev1, int rev2, SituationFamiliale sit, int enfants, int enfantsHandicapes, boolean isole) {
        if (rev1 < 0 || rev2 < 0)
            throw new IllegalArgumentException("Les revenus ne peuvent pas être négatifs.");
        if (sit == null)
            throw new IllegalArgumentException("La situation familiale est obligatoire.");
        if (enfants < 0 || enfantsHandicapes < 0)
            throw new IllegalArgumentException("Le nombre d'enfants ne peut être négatif.");
        if (enfantsHandicapes > enfants)
            throw new IllegalArgumentException("Les enfants handicapés ne peuvent excéder le nombre total d'enfants.");
        if (enfants > 7)
            throw new IllegalArgumentException("Limite maximale : 7 enfants pris en compte.");
        if ((sit == SituationFamiliale.MARIE || sit == SituationFamiliale.PACSE) && isole)
            throw new IllegalArgumentException("Un parent isolé ne peut être marié ou pacsé.");
        if ((sit == SituationFamiliale.CELIBATAIRE || sit == SituationFamiliale.DIVORCE || sit == SituationFamiliale.VEUF) && rev2 > 0)
            throw new IllegalArgumentException("Un contribuable seul ne peut avoir deux revenus.");
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
    private void initialisation(int rev1, int rev2, SituationFamiliale sit, int enfants, int enfantsHandicapes, boolean isole) {
        this.revenu1 = rev1;
        this.revenu2 = rev2;
        this.nbEnfants = enfants;
        this.nbEnfantsHandicap = enfantsHandicapes;
        this.parentIsole = isole;
        this.situation = sit;
    }
}
