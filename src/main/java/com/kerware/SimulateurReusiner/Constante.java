package com.kerware.SimulateurReusiner;

public class Constante {
    // Tranches de l'impôt de base
    public static final int[] TRANCHES_IMPOSITION = {0, 11294, 28797, 82341, 177106, Integer.MAX_VALUE};
    public static final double[] TAUX_TRANCHES = {0.0, 0.11, 0.30, 0.41, 0.45};
    
    // Tranches pour la Contribution Exceptionnelle sur les Hauts Revenus (CEHR)
    public static final int[] TRANCHES_CEHR = {0, 250000, 500000, 1000000, Integer.MAX_VALUE};
    public static final double[] TAUX_CEHR_CELIBATAIRE = {0.0, 0.03, 0.04, 0.04};
    public static final double[] TAUX_CEHR_COUPLE = {0.0, 0.0, 0.03, 0.04};
    
    // Paramètres de l'abattement
    public static final double TAUX_ABATTEMENT = 0.10;
    public static final int ABATTEMENT_MIN = 495;
    public static final int ABATTEMENT_MAX = 14171;
    
    // Paramètres de la décote
    public static final double SEUIL_DECOTE_CELIBATAIRE = 1929;
    public static final double SEUIL_DECOTE_COUPLE = 3191;
    public static final double DECOTE_MAX_CELIBATAIRE = 873;
    public static final double DECOTE_MAX_COUPLE = 1444;
    public static final double TAUX_DECOTE = 0.4525;
    
    // Plafonnement du quotient familial
    public static final double PLAFOND_PAR_DEMI_PART = 1759;
}
