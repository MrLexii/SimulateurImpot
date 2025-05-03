package com.kerware.SimulateurReusiner;

import com.kerware.simulateur.SituationFamiliale;

/**
 * Cette classe permet de simuler le calcul de l'impôt sur le revenu
 * en France pour l'année 2024 sur les revenus de l'année 2023.
 * Elle prend en charge différentes situations familiales, le nombre d'enfants,
 * la situation de parent isolé, ainsi que les enfants en situation de handicap.
 *
 * Ce code est un exemple de refactoring d'un legacy code pour le rendre plus
 * maintenable tout en conservant sa logique d'origine.
 */
public class SimulateurReusiner {
	
	// Appel a nos classes
	private CalculAbattement calculAbattement;
    private CalculPartsFiscales calculPartsFiscales;
    private CalculContributionExceptionnelle calculContributionExceptionnelle;
    private CalculImpots calculImpots;
    private CalculPlafondQuotientFamille calculPlafondQuotientFamille;
    private Decote decote;

    // Données d'entrée
    private int revenu1;
    private int revenu2;
    private int nbEnfants;
    private int nbEnfantsHandicap;
    private boolean parentIsole;
    private SituationFamiliale situation;

    // Données général
    private double revenuFiscalReference;
    private double abattement;
    private double nbPartsDeclarant;
    private double nbPartsFoyerFiscal;
    private double variableDecote;
    private double impotFoyerFiscal;
    private double impotFoyerFiscalAvantDecote;
    private double contributionExceptionnelle;
    
    
    // Getters pour adapter le code legacy pour les tests unitaires
    public double getRevenuReference() {return revenuFiscalReference;}
    public double getDecote() {return variableDecote;}
    public double getAbattement() {return abattement;}
    public double getNbPartsFoyerFiscal() {return nbPartsFoyerFiscal;}
    public double getImpotAvantDecote() {return impotFoyerFiscalAvantDecote;}
    public double getImpotNet() {return impotFoyerFiscal;}
    public int getRevenuNetDeclatant1() {return revenu1;}
    public int getRevenuNetDeclatant2() {return revenu2;}
    public double getContributionExceptionnelle() {return contributionExceptionnelle;}
    
    /**
     * Constructeur de la classe SimulateurReusiner.
     * Initialise toutes les classes nécessaires pour effectuer les calculs d'impôt.
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
     * Méthode principale pour calculer l'impôt à partir des informations de revenu et de situation.
     * Cette méthode orchestre les différents calculs et retourne l'impôt calculé.
     * @param rev1 Revenu principal.
     * @param rev2 Revenu secondaire.
     * @param sit Situation familiale (Célibataire, Marié, etc.).
     * @param enfants Nombre d'enfants.
     * @param enfantsHandicapes Nombre d'enfants handicapés.
     * @param isole Indicateur si le contribuable est isolé.
     * @return Montant de l'impôt calculé.
     */
    public int calculerImpot(int rev1, int rev2, SituationFamiliale sit, int enfants, int enfantsHandicapes, boolean isole) {
      
    	System.out.println("=========== DÉBUT DU CALCUL DE L'IMPÔT ===========");
        System.out.printf("Entrées : Revenu1 = %d €, Revenu2 = %d €, Situation = %s, Enfants = %d, Enfants handicapés = %d, Parent isolé = %s%n",
                rev1, rev2, sit, enfants, enfantsHandicapes, isole);

    	
    	// Étape 1 : Vérification des données d’entrée
        validations(rev1, rev2, sit, enfants, enfantsHandicapes, isole);

        // Étape 2 : Stockage des données d'entrée dans les variables de classe
        initialisation(rev1, rev2, sit, enfants, enfantsHandicapes, isole);

        // Étape 3 : Calcul de l’abattement de 10% (avec plancher/plafond)
        calculAbattement.calculerAbattement(this.revenu1, this.revenu2, this.situation);

        this.abattement = calculAbattement.getAbattement();
        this.revenuFiscalReference = calculAbattement.getRevenuFiscalRef();
        
        System.out.printf("→ Abattement total : %.2f €%n", this.abattement);
        System.out.printf("→ Revenu fiscal de référence : %.2f €%n", this.revenuFiscalReference);

        // Étape 4 : Calcul du nombre de parts fiscales
        calculPartsFiscales.calculerParts(this.nbEnfants, this.situation, this.parentIsole, this.nbEnfantsHandicap);

        this.nbPartsDeclarant = calculPartsFiscales.getPartsDeclarant();
        this.nbPartsFoyerFiscal  = calculPartsFiscales.getPartsFoyerFiscal();
        
        System.out.printf("→ Parts Déclarants : %.2f%n", this.nbPartsDeclarant);
        System.out.printf("→ Parts fiscales (après majorations) : %.2f%n", this.nbPartsFoyerFiscal);

        // Étape 5 : Calcul de la contribution exceptionnelle sur les hauts revenus
        calculContributionExceptionnelle.calculerContribution(this.revenuFiscalReference, this.nbPartsDeclarant);

        this.contributionExceptionnelle = calculContributionExceptionnelle.getContributionExceptionnelle();
        
        System.out.printf("→ Contribution exceptionnelle CEHR : %.2f €%n", this.contributionExceptionnelle);

        // Étape 6 : Calcul de l’impôt sans plafonnement du quotient familial
        double impDec1 = calculImpots.calculImpôtAvantPlafond(revenuFiscalReference, nbPartsDeclarant);

        System.out.printf("→ Impôt sans quotient familial : %.2f €%n", impDec1);

        // Étape 7 : Calcul de l’impôt avec quotient familial
        this.impotFoyerFiscal = calculImpots.calculImpôtFoyer(revenuFiscalReference, nbPartsFoyerFiscal);

        System.out.printf("→ Impôt avec quotient familial : %.2f €%n", this.impotFoyerFiscal);

        // Étape 8 : Appliquer le plafonnement du quotient familial
        this.impotFoyerFiscal = calculPlafondQuotientFamille.calculPlafonnementQuotientFamilial(impDec1, this.impotFoyerFiscal, this.nbPartsDeclarant, this.nbPartsFoyerFiscal);

        System.out.printf("→ Baisse d'Impôt : %.2f €%n", calculPlafondQuotientFamille.getBaisseImpot());
        System.out.printf("→ Plafond : %.2f €%n", calculPlafondQuotientFamille.getPlafond());
        System.out.printf("→ Impôt après plafonnement quotient familial : %.2f €%n", this.impotFoyerFiscal);

        // Étape 9 : Appliquer la décote selon la situation
        this.impotFoyerFiscal = decote.calculDecote(this.impotFoyerFiscal, this.nbPartsDeclarant, this.contributionExceptionnelle);

        System.out.printf("→ Décote appliquée : %.2f €%n", decote.getValeur());
        System.out.printf("→ Impôt net à payer (incl. CEHR) : %.2f €%n", this.impotFoyerFiscal);

        System.out.println("=========== FIN DU CALCUL ===========\n");
        
        // Étape 10 : Retourner le montant final de l’impôt (arrondi)
        return (int) this.impotFoyerFiscal;
    }
    
    /**
     * Valide les entrées fournies pour le calcul de l'impôt.
     *
     * @param rev1 Revenu du déclarant 1
     * @param rev2 Revenu du déclarant 2
     * @param sit Situation familiale
     * @param enfants Nombre d’enfants à charge
     * @param enfantsHandicapes Nombre d’enfants handicapés à charge
     * @param isole Indique si le déclarant est un parent isolé
     * @throws IllegalArgumentException si une des validations échoue
     */
    private void validations(int rev1, int rev2, SituationFamiliale sit, int enfants, int enfantsHandicapes, boolean isole) {
        if (rev1 < 0 || rev2 < 0)
            throw new IllegalArgumentException("Les revenus ne peuvent pas être négatifs.");
        if (sit == null)
            throw new IllegalArgumentException("La situation familiale doit être renseignée.");
        if (enfants < 0 || enfantsHandicapes < 0)
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être négatif.");
        if (enfantsHandicapes > enfants)
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne peut pas dépasser le nombre total d'enfants.");
        if (enfants > 7)
            throw new IllegalArgumentException("Le simulateur ne prend pas en charge plus de 7 enfants.");
        if ((sit == SituationFamiliale.MARIE || sit == SituationFamiliale.PACSE) && isole)
            throw new IllegalArgumentException("Un parent isolé ne peut pas être marié ou pacsé.");
        if ((sit == SituationFamiliale.CELIBATAIRE || sit == SituationFamiliale.DIVORCE || sit == SituationFamiliale.VEUF) && rev2 > 0)
            throw new IllegalArgumentException("Un déclarant seul ne peut avoir de second revenu.");
    }

    /**
     * Initialise les variables d'instance avec les valeurs d'entrée.
     *
     * @param rev1 Revenu du déclarant 1
     * @param rev2 Revenu du déclarant 2
     * @param sit Situation familiale
     * @param enfants Nombre d’enfants à charge
     * @param enfantsHandicapes Nombre d’enfants handicapés à charge
     * @param isole Vrai si le déclarant est un parent isolé
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
