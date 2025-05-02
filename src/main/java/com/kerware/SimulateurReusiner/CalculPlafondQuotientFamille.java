package com.kerware.SimulateurReusiner;

public class CalculPlafondQuotientFamille {
	
	private double partsFiscales;
	private double partsContribuable;
	
	public CalculPlafondQuotientFamille(double impotDeclarant, double impotFoyerFiscal) {
		this.partsContribuable = impotDeclarant;
		this.partsFiscales = impotFoyerFiscal;
	}

	/**
     * Applique le plafonnement du quotient familial si l’avantage fiscal est trop élevé.
     * @param impDec1 Impôt sans quotient familial
     * @param impFoy Impôt avec quotient familial
     * @return Impôt ajusté après application du plafonnement
     * EXIGENCE : Le plafonnement est appliqué si l'avantage fiscal dépasse un certain seuil par demi-part fiscale.
     */
    double calculPlafonnementQuotientFamilial(double impDec1, double impFoy) {
        double baisseImpot = impDec1 - impFoy;
        double ecartPts = partsFiscales - partsContribuable;
        double plafond = (ecartPts / 0.5) * Constante.PLAFOND_PAR_DEMI_PART;

        if (baisseImpot >= plafond) {
            impFoy = impDec1 - plafond;
        }

        return impFoy;
    }
}