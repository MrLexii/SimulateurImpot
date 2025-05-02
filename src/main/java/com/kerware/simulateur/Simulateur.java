package com.kerware.simulateur;

/**
 * Cette classe permet de simuler le calcul de l'impôt sur le revenu
 * en France pour l'année 2024 sur les revenus de l'année 2023.
 * Elle prend en charge différentes situations familiales, le nombre d'enfants,
 * la situation de parent isolé, ainsi que les enfants en situation de handicap.
 *
 * Ce code est un exemple de refactoring d'un legacy code pour le rendre plus
 * maintenable tout en conservant sa logique d'origine.
 */
public class Simulateur {

    // Tranches d'imposition pour le barème progressif (2023)
    private final int[] tranches = {0, 11294, 28797, 82341, 177106, Integer.MAX_VALUE};
    private final double[] tauxTranches = {0.0, 0.11, 0.30, 0.41, 0.45};

    // Tranches et taux pour la Contribution Exceptionnelle sur les Hauts Revenus (CEHR)
    private final int[] tranchesCEHR = {0, 250000, 500000, 1000000, Integer.MAX_VALUE};
    private final double[] tauxCEHRCelibataire = {0.0, 0.03, 0.04, 0.04};
    private final double[] tauxCEHRCouple = {0.0, 0.0, 0.03, 0.04};

    // Paramètres pour l'abattement de 10% avec minimum et maximum
    private final double tauxAbattement = 0.10;
    private final int abattementMin = 495;
    private final int abattementMax = 14171;

    // Paramètres pour la décote
    private final double seuilDecoteCelibataire = 1929;
    private final double seuilDecoteCouple = 3191;
    private final double decoteMaxCelibataire = 873;
    private final double decoteMaxCouple = 1444;
    private final double tauxDecote = 0.4525;

    // Plafond de baisse d’impôt liée au quotient familial
    private final double plafondParDemiPart = 1759;

    // Données d'entrée
    private int revenu1;
    private int revenu2;
    private int nbEnfants;
    private int nbEnfantsHandicap;
    private boolean parentIsole;
    private SituationFamiliale situation;

    // Données calculées
    private double revenuFiscalRef;
    private double abattement;
    private double partsFiscales;
    private double partsContribuable;
    private double impotBrut;
    private double decote;
    private double impotNet;
    private double contributionExceptionnelle;

    // ---------------------- GETTERS POUR TESTS ----------------------

    public double getRevenuReference() { return revenuFiscalRef; }
    public double getDecote() { return decote; }
    public double getAbattement() { return abattement; }
    public double getNbParts() { return partsFiscales; }
    public double getImpotAvantDecote() { return impotBrut; }
    public double getImpotNet() { return impotNet; }
    public double getcontributionExceptionnelle() { return contributionExceptionnelle; }

    /**
     * Calcule l'impôt sur le revenu pour un foyer fiscal donné.
     * Suit toutes les étapes du calcul : abattement, parts, plafonnement,
     * décote et contribution exceptionnelle.
     */
    public int calculImpot(int rev1, int rev2, SituationFamiliale sit, int enfants, int enfantsHandicapes, boolean isole) {
        
    	System.out.println("=========== DÉBUT DU CALCUL DE L'IMPÔT ===========");
        System.out.printf("Entrées : Revenu1 = %d €, Revenu2 = %d €, Situation = %s, Enfants = %d, Enfants handicapés = %d, Parent isolé = %s%n",
                rev1, rev2, sit, enfants, enfantsHandicapes, isole);

    	
    	// Étape 1 : Vérification des données d’entrée
        validations(rev1, rev2, sit, enfants, enfantsHandicapes, isole);

        // Étape 2 : Stockage des données d'entrée dans les variables de classe
        initialisation(rev1, rev2, sit, enfants, enfantsHandicapes, isole);

        // Étape 3 : Calcul de l’abattement de 10% (avec plancher/plafond)
        calculAbattement();

        System.out.printf("→ Abattement total : %.2f €%n", abattement);
        System.out.printf("→ Revenu fiscal de référence : %.2f €%n", revenuFiscalRef);

        // Étape 4 : Calcul du nombre de parts fiscales
        calculPartsFiscal();

        System.out.printf("→ Parts contribuable : %.2f%n", partsContribuable);
        System.out.printf("→ Parts fiscales (après majorations) : %.2f%n", partsFiscales);

        // Étape 5 : Calcul de la contribution exceptionnelle sur les hauts revenus
        calculContributionExceptionnelle();

        System.out.printf("→ Contribution exceptionnelle CEHR : %.2f €%n", contributionExceptionnelle);

        // Étape 6 : Calcul de l’impôt sans plafonnement du quotient familial
        double impDec1 = calculImpôtAvantPlafond();

        System.out.printf("→ Impôt sans quotient familial : %.2f €%n", impDec1);

        // Étape 7 : Calcul de l’impôt avec quotient familial
        double impFoy = calculImpôtFoyer();

        System.out.printf("→ Impôt avec quotient familial : %.2f €%n", impFoy);

        // Étape 8 : Appliquer le plafonnement du quotient familial
        impFoy = calculPlafonnementQuotientFamilial(impDec1, impFoy);

        System.out.printf("→ Impôt après plafonnement quotient familial : %.2f €%n", impFoy);

        // Étape 9 : Appliquer la décote selon la situation
        double impotFinal = calculDecote(impFoy);

        System.out.printf("→ Décote appliquée : %.2f €%n", decote);
        System.out.printf("→ Impôt net à payer (incl. CEHR) : %.2f €%n", impotFinal);

        System.out.println("=========== FIN DU CALCUL ===========\n");
        
        // Étape 10 : Retourner le montant final de l’impôt (arrondi)
        return (int) impotFinal;
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

    /**
     * Calcule l’abattement de 10 % sur les revenus et met à jour le revenu fiscal de référence.
     * Ne retourne rien mais affecte les champs {@code abattement} et {@code revenuFiscalRef}.
     * EXIGENCE : L'abattement est appliqué à chaque revenu du déclarant avec un minimum de 495€ et un maximum de 14171€.
     */
    private void calculAbattement() {
        long abattement1 = Math.round(revenu1 * tauxAbattement);
        long abattement2 = Math.round(revenu2 * tauxAbattement);

        abattement1 = Math.min(Math.max(abattement1, abattementMin), abattementMax);
        if (situation == SituationFamiliale.MARIE || situation == SituationFamiliale.PACSE) {
            abattement2 = Math.min(Math.max(abattement2, abattementMin), abattementMax);
        } else {
            abattement2 = 0;
        }

        abattement = abattement1 + abattement2;
        revenuFiscalRef = Math.max(0, revenu1 + revenu2 - abattement);
    }

    /**
     * Calcule le nombre de parts fiscales selon la situation familiale et les enfants.
     * Ne retourne rien mais met à jour les champs {@code partsFiscales} et {@code partsContribuable}.
     */
    private void calculPartsFiscal() {
        // Base : 1 part ou 2 parts selon la situation
        partsContribuable = (situation == SituationFamiliale.MARIE || situation == SituationFamiliale.PACSE) ? 2 : 1;

        // Ajout des parts liées aux enfants
        if (nbEnfants <= 2) {
            partsFiscales = partsContribuable + nbEnfants * 0.5;
        } else {
            partsFiscales = partsContribuable + 1.0 + (nbEnfants - 2);
        }

        // Majoration pour parent isolé avec enfant
        if (parentIsole && nbEnfants > 0) {
            partsFiscales += 0.5;
        }

        // Majoration pour veuf(ve) avec enfant
        if (situation == SituationFamiliale.VEUF && nbEnfants > 0) {
            partsFiscales += 1.0;
        }

        // Majoration pour enfants handicapés
        partsFiscales += nbEnfantsHandicap * 0.5;
    }

    /**
     * Calcule la contribution exceptionnelle sur les hauts revenus (CEHR) selon les tranches.
     * Ne retourne rien mais met à jour le champ {@code contributionExceptionnelle}.
     * EXIGENCE : La CEHR est calculée en fonction des tranches de revenus et du statut du contribuable (célibataire ou couple).
     */
    private void calculContributionExceptionnelle() {
        contributionExceptionnelle = 0;
        int i = 0;
        do {
            if (revenuFiscalRef >= tranchesCEHR[i] && revenuFiscalRef < tranchesCEHR[i + 1]) {
                contributionExceptionnelle += (revenuFiscalRef - tranchesCEHR[i]) *
                        (partsContribuable == 1 ? tauxCEHRCelibataire[i] : tauxCEHRCouple[i]);
                break;
            } else {
                contributionExceptionnelle += (tranchesCEHR[i + 1] - tranchesCEHR[i]) *
                        (partsContribuable == 1 ? tauxCEHRCelibataire[i] : tauxCEHRCouple[i]);
            }
            i++;
        } while (i < tranchesCEHR.length - 1);
        contributionExceptionnelle = Math.round(contributionExceptionnelle);
    }

    /**
     * Calcule l’impôt du foyer selon la méthode de référence (sans quotient familial).
     *
     * @return Montant de l'impôt brut avant application du quotient familial
     * EXIGENCE : La CEHR est calculée en fonction des tranches de revenus et du statut du contribuable (célibataire ou couple).
     */
    private double calculImpôtAvantPlafond() {
        double rImposable = revenuFiscalRef / partsContribuable;
        double mImpDecl = 0;
        int i = 0;
        do {
            if (rImposable >= tranches[i] && rImposable < tranches[i + 1]) {
                mImpDecl += (rImposable - tranches[i]) * tauxTranches[i];
                break;
            } else {
                mImpDecl += (tranches[i + 1] - tranches[i]) * tauxTranches[i];
            }
            i++;
        } while (i < tranches.length - 1);
        mImpDecl *= partsContribuable;
        mImpDecl = Math.round(mImpDecl);
        return mImpDecl;
    }

    /**
     * Calcule l’impôt du foyer en tenant compte du quotient familial (revenu par part).
     *
     * @return Montant de l’impôt calculé avec le quotient familial
     * EXIGENCE : L’impôt doit être calculé par part fiscale en tenant compte des tranches d’imposition et du quotient familial.
     */
    private double calculImpôtFoyer() {
        double rffImposable = revenuFiscalRef / partsFiscales;
        double ffmImp = 0;
        int i = 0;
        do {
            if (rffImposable >= tranches[i] && rffImposable < tranches[i + 1]) {
                ffmImp += (rffImposable - tranches[i]) * tauxTranches[i];
                break;
            } else {
                ffmImp += (tranches[i + 1] - tranches[i]) * tauxTranches[i];
            }
            i++;
        } while (i < tranches.length - 1);
        ffmImp *= partsFiscales;
        ffmImp = Math.round(ffmImp);
        return ffmImp;
    }

    /**
     * Applique le plafonnement du quotient familial si l’avantage fiscal est trop élevé.
     *
     * @param impDec1 Impôt sans quotient familial
     * @param impFoy Impôt avec quotient familial
     * @return Impôt ajusté après application du plafonnement
     * EXIGENCE : Le plafonnement est appliqué si l'avantage fiscal dépasse un certain seuil par demi-part fiscale.
     */
    private double calculPlafonnementQuotientFamilial(double impDec1, double impFoy) {
        double baisseImpot = impDec1 - impFoy;
        double ecartPts = partsFiscales - partsContribuable;
        double plafond = (ecartPts / 0.5) * plafondParDemiPart;

        if (baisseImpot >= plafond) {
            impFoy = impDec1 - plafond;
        }

        impotBrut = impFoy;
        return impFoy;
    }

    /**
     * Calcule la décote si l’impôt est inférieur à un certain seuil, puis ajoute la CEHR.
     *
     * @param impFoy Impôt après quotient familial et plafonnement
     * @return Impôt net à payer après décote et CEHR
     * EXIGENCE : Si l’impôt est inférieur au seuil de décote, une réduction est appliquée et le montant net à payer est ajusté.
     */
    private double calculDecote(double impFoy) {
        decote = 0;
        if (partsContribuable == 1 && impFoy < seuilDecoteCelibataire) {
            decote = decoteMaxCelibataire - (impFoy * tauxDecote);
        } else if (partsContribuable == 2 && impFoy < seuilDecoteCouple) {
            decote = decoteMaxCouple - (impFoy * tauxDecote);
        }

        decote = Math.round(decote);
        if (impFoy <= decote) {
            decote = impFoy;
        }

        impFoy -= decote;
        impFoy += contributionExceptionnelle;
        impotNet = Math.round(impFoy);
        return impotNet;
    }
}
