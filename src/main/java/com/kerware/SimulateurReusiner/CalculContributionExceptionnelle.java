package com.kerware.SimulateurReusiner;

public class CalculContributionExceptionnelle {
	
    private double contributionExceptionnelle;

	/**
     * Calcule la contribution exceptionnelle sur les hauts revenus (CEHR) en fonction du revenu fiscal de référence
     * et du nombre de parts fiscales.
     * @param revenuFiscalRef Le revenu fiscal de référence du contribuable.
     * @param nbPartsDeclarant Le nombre de parts fiscales du contribuable.
     * EXIGENCE : La CEHR est calculée en fonction des tranches de revenus et du statut du contribuable (célibataire ou couple).
     */
    public void calculerContribution(double revenuFiscalRef, double nbPartsDeclarant) {
    	contributionExceptionnelle = 0;
        int i = 0;
        do {
            if ( revenuFiscalRef >= Constante.TRANCHES_CEHR[i] && revenuFiscalRef < Constante.TRANCHES_CEHR[i+1] ) {
                if ( nbPartsDeclarant == 1 ) {
                	contributionExceptionnelle += ( revenuFiscalRef - Constante.TRANCHES_CEHR[i] ) * Constante.TAUX_CEHR_CELIBATAIRE[i];
                } else {
                	contributionExceptionnelle += ( revenuFiscalRef - Constante.TRANCHES_CEHR[i] ) * Constante.TAUX_CEHR_COUPLE[i];
                }
                break;
            } else {
                if ( nbPartsDeclarant == 1 ) {
                	contributionExceptionnelle += ( Constante.TRANCHES_CEHR[i+1] - Constante.TRANCHES_CEHR[i] ) * Constante.TAUX_CEHR_CELIBATAIRE[i];
                } else {
                	contributionExceptionnelle += ( Constante.TRANCHES_CEHR[i+1] - Constante.TRANCHES_CEHR[i] ) * Constante.TAUX_CEHR_COUPLE[i];
                }
            }
            i++;
        } while( i < 5);

        contributionExceptionnelle = Math.round( contributionExceptionnelle );
    }

    /**
     * Getter pour obtenir le montant de la contribution exceptionnelle.
     * @return Le montant de la contribution exceptionnelle.
     */
    public double getContributionExceptionnelle() {
        return contributionExceptionnelle;
    }
}
