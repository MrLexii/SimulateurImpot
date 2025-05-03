package com.kerware.SimulateurReusiner;

/**
 * Cette classe applique le plafonnement du quotient familial conformément à la réglementation fiscale française.
 *
 * <p><b>Exigences couvertes :</b></p>
 * <ul>
 *   <li>EM-006 : Limitation de l'avantage fiscal par demi-part supplémentaire</li>
 *   <li>EM-007 : Comparaison entre impôt sans quotient et impôt avec quotient pour plafonner le gain</li>
 * </ul>
 */
public class CalculPlafondQuotientFamille {

    private double baisseImpot;  // Avantage fiscal obtenu par le quotient familial
    private double plafond;      // Plafond maximal autorisé pour cet avantage

    /**
     * Calcule l’impôt ajusté après application du plafonnement du quotient familial.
     *
     * <p>Le gain d'impôt obtenu grâce aux parts supplémentaires (enfants, parent isolé...) est plafonné.
     * Ce plafonnement dépend du nombre de demi-parts supplémentaires et d’un plafond fixé par l’administration fiscale.</p>
     *
     * @param impDec1 Impôt sans quotient familial (1 ou 2 parts selon situation)
     * @param impFoy Impôt après application du quotient familial (toutes parts incluses)
     * @param nbPartsDeclarant Nombre de parts du déclarant (hors enfants et avantages)
     * @param nbPartsFoyerFiscal Nombre total de parts fiscales (incluant enfants, handicap, etc.)
     * @return Impôt éventuellement majoré si le gain excède le plafond autorisé
     */
    public double calculPlafonnementQuotientFamilial(
        double impDec1,
        double impFoy,
        double nbPartsDeclarant,
        double nbPartsFoyerFiscal
    ) {
        // Calcul du gain d'impôt lié aux parts supplémentaires
        baisseImpot = impDec1 - impFoy;

        // Calcul du plafond selon le nombre de demi-parts en plus
        double ecartPts = nbPartsFoyerFiscal - nbPartsDeclarant;
        plafond = (ecartPts / 0.5) * Constante.PLAFOND_PAR_DEMI_PART;

        // Si le gain est supérieur au plafond, on le réduit à ce maximum
        if (baisseImpot >= plafond) {
            impFoy = impDec1 - plafond;
        }

        return impFoy;
    }

    /**
     * @return Avantage fiscal brut (avant éventuel plafonnement)
     */
    public double getBaisseImpot() {
        return baisseImpot;
    }

    /**
     * @return Plafond appliqué à l’avantage fiscal en fonction du nombre de demi-parts
     */
    public double getPlafond() {
        return plafond;
    }
}
