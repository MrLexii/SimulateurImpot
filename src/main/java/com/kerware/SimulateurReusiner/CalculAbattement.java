package com.kerware.SimulateurReusiner;

import com.kerware.simulateur.SituationFamiliale;

public class CalculAbattement {

	private double abattement;
    private double abattement1;
    private double abattement2;
    private double revenuFiscalRef;

	/**
     * Calcule l'abattement appliqué aux revenus en fonction de la situation fiscale du contribuable.
     * @param revenu1 Revenu principal du contribuable.
     * @param revenu2 Revenu secondaire (si applicable).
     * @param situation Situation familiale du contribuable (célibataire, marié, etc.).
     * EXIGENCE : L'abattement est appliqué à chaque revenu du déclarant avec un minimum de 495€ et un maximum de 14171€.
     */
    public void calculerAbattement(int revenu1, int revenu2, SituationFamiliale situation) {
    	abattement1 = Math.round(revenu1 * Constante.TAUX_ABATTEMENT);
        abattement2 = Math.round(revenu2 * Constante.TAUX_ABATTEMENT);

        abattement1 = Math.min(Math.max(abattement1, Constante.ABATTEMENT_MIN), Constante.ABATTEMENT_MAX);
        if (situation == SituationFamiliale.MARIE || situation == SituationFamiliale.PACSE) {
            abattement2 = Math.min(Math.max(abattement2, Constante.ABATTEMENT_MIN), Constante.ABATTEMENT_MAX);
        } else {
            abattement2 = 0;
        }

        abattement = abattement1 + abattement2;
        revenuFiscalRef = Math.max(0, revenu1 + revenu2 - abattement);
    }

    /**
     * Getter pour obtenir l'abattement total.
     * @return L'abattement total.
     */
    public double getAbattement() {
        return abattement;
    }

    /**
     * Getter pour obtenir le revenu fiscal de référence.
     * @return Le revenu fiscal de référence après abattement.
     */
    public double getRevenuFiscalRef() {
        return revenuFiscalRef;
    }
}
