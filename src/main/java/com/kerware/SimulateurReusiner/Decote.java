package com.kerware.SimulateurReusiner;

/**
 * Cette classe calcule la décote applicable à l’impôt sur le revenu
 * lorsque celui-ci est inférieur à un seuil fixé par l'administration fiscale.
 *
 * <p>La décote permet de réduire voire annuler l’impôt dû, principalement pour les foyers à faibles revenus.
 * Elle est ensuite ajoutée à la Contribution Exceptionnelle sur les Hauts Revenus (CEHR) le cas échéant.</p>
 */
public class Decote {

    /**
     * Montant de la décote appliquée, calculé lors du traitement.
     */
    private double decote;

    /**
     * Calcule la décote à appliquer si l’impôt brut est inférieur à un seuil.
     * Puis ajoute la CEHR pour obtenir l’impôt net final.
     *
     * @param impFoy Impôt calculé après quotient familial et plafonnement.
     * @param nbPartsDeclarant Nombre de parts du ou des déclarants (hors enfants).
     * @param contributionExceptionnelle Montant de la Contribution Exceptionnelle sur les Hauts Revenus (CEHR).
     * @return Montant de l’impôt net à payer, après application de la décote et ajout de la CEHR.
     *
     * <p><strong>EXIGENCE :</strong> Le calcul suit les seuils et plafonds de décote définis pour les célibataires et les couples.</p>
     */
    public double calculDecote(double impFoy, double nbPartsDeclarant, double contributionExceptionnelle) {
        decote = 0;

        // Application des règles de décote selon le nombre de parts
        if (nbPartsDeclarant == 1 && impFoy < Constante.SEUIL_DECOTE_CELIBATAIRE) {
            decote = Constante.DECOTE_MAX_CELIBATAIRE - (impFoy * Constante.TAUX_DECOTE);
        } else if (nbPartsDeclarant == 2 && impFoy < Constante.SEUIL_DECOTE_COUPLE) {
            decote = Constante.DECOTE_MAX_COUPLE - (impFoy * Constante.TAUX_DECOTE);
        }

        decote = Math.round(decote);

        // La décote ne peut pas être supérieure à l’impôt dû
        if (impFoy <= decote) {
            decote = impFoy;
        }

        // Calcul de l’impôt net final après décote et ajout de la CEHR
        impFoy -= decote;
        impFoy += contributionExceptionnelle;

        return Math.round(impFoy);
    }

    /**
     * Récupère la valeur de la décote calculée.
     *
     * @return Montant de la décote appliquée.
     */
    public double getValeur() {
        return decote;
    }
}
