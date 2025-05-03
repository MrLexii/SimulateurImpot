package com.kerware.SimulateurReusiner;

public class Decote {

	private double decote;

	/**
     * Calcule la décote si l’impôt est inférieur à un certain seuil, puis ajoute la CEHR.
     *
     * @param impFoy Impôt après quotient familial et plafonnement
     * @return Impôt net à payer après décote et CEHR
     * EXIGENCE : Si l’impôt est inférieur au seuil de décote, une réduction est appliquée et le montant net à payer est ajusté.
     */
    public double calculDecote(double impFoy, double nbPartsDeclarant, double contributionExceptionnelle) {
        decote = 0;
        if (nbPartsDeclarant == 1 && impFoy < Constante.SEUIL_DECOTE_CELIBATAIRE) {
            decote = Constante.DECOTE_MAX_CELIBATAIRE - (impFoy * Constante.TAUX_DECOTE);
        } else if (nbPartsDeclarant == 2 && impFoy < Constante.SEUIL_DECOTE_COUPLE) {
            decote = Constante.DECOTE_MAX_COUPLE - (impFoy * Constante.TAUX_DECOTE);
        }

        decote = Math.round(decote);
        if (impFoy <= decote) {
            decote = impFoy;
        }

        impFoy -= decote;
        impFoy += contributionExceptionnelle;
        double impotNet = Math.round(impFoy);
        return impotNet;
    }
    
    public double getValeur() {
    	return decote;
    }
    
}
