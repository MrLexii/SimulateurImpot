package com.kerware.SimulateurReusiner;

/**
 * Cette classe centralise toutes les constantes utilisées dans le calcul de l'impôt sur le revenu,
 * y compris les tranches d’imposition, les taux, les seuils pour la décote, la CEHR et le quotient familial.
 *
 * <p>Elle permet une gestion centralisée et claire des paramètres fiscaux applicables à chaque campagne.</p>
 */
public class Constante {

    // ---------------------------------------------------
    // Tranches et taux d’imposition sur le revenu (barème progressif)
    // ---------------------------------------------------

    /**
     * Bornes des tranches du barème progressif d’imposition (valeurs en euros).
     */
    public static final int[] TRANCHES_IMPOSITION = {
        0, 11294, 28797, 82341, 177106, Integer.MAX_VALUE
    };

    /**
     * Taux d’imposition correspondant à chaque tranche.
     */
    public static final double[] TAUX_TRANCHES = {
        0.0, 0.11, 0.30, 0.41, 0.45
    };


    // ---------------------------------------------------
    // Contribution Exceptionnelle sur les Hauts Revenus (CEHR)
    // ---------------------------------------------------

    /**
     * Bornes des tranches de revenu pour le calcul de la CEHR.
     */
    public static final int[] TRANCHES_CEHR = {
        0, 250000, 500000, 1000000, Integer.MAX_VALUE
    };

    /**
     * Taux applicables à chaque tranche pour un contribuable célibataire.
     */
    public static final double[] TAUX_CEHR_CELIBATAIRE = {
        0.0, 0.03, 0.04, 0.04
    };

    /**
     * Taux applicables à chaque tranche pour un couple (marié/PACS).
     */
    public static final double[] TAUX_CEHR_COUPLE = {
        0.0, 0.0, 0.03, 0.04
    };


    // ---------------------------------------------------
    // Paramètres d’abattement sur le revenu net
    // ---------------------------------------------------

    /**
     * Taux d’abattement forfaitaire sur les revenus nets déclarés.
     */
    public static final double TAUX_ABATTEMENT = 0.10;

    /**
     * Abattement minimum (en euros).
     */
    public static final int ABATTEMENT_MIN = 495;

    /**
     * Abattement maximum (en euros).
     */
    public static final int ABATTEMENT_MAX = 14171;


    // ---------------------------------------------------
    // Paramètres de la décote
    // ---------------------------------------------------

    /**
     * Seuil d’imposition en dessous duquel une décote s’applique (célibataire).
     */
    public static final double SEUIL_DECOTE_CELIBATAIRE = 1929;

    /**
     * Seuil d’imposition en dessous duquel une décote s’applique (couple).
     */
    public static final double SEUIL_DECOTE_COUPLE = 3191;

    /**
     * Montant maximal de la décote pour un célibataire.
     */
    public static final double DECOTE_MAX_CELIBATAIRE = 873;

    /**
     * Montant maximal de la décote pour un couple.
     */
    public static final double DECOTE_MAX_COUPLE = 1444;

    /**
     * Taux de calcul de la décote.
     */
    public static final double TAUX_DECOTE = 0.4525;


    // ---------------------------------------------------
    // Plafonnement du quotient familial
    // ---------------------------------------------------

    /**
     * Plafond d’avantage fiscal par demi-part supplémentaire (en euros).
     */
    public static final double PLAFOND_PAR_DEMI_PART = 1759;
}
