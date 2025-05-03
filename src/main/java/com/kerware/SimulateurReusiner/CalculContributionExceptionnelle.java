package com.kerware.SimulateurReusiner;

/**
 * Cette classe permet de calculer la Contribution Exceptionnelle sur les Hauts Revenus (CEHR),
 * en fonction du revenu fiscal de référence et du nombre de parts fiscales.
 *
 * <p><b>Exigences couvertes :</b></p>
 * <ul>
 *   <li>METIER-001 : Respect des règles fiscales 2024 (base DGFiP)</li>
 *   <li>METIER-002 : Prise en compte de la CEHR</li>
 *   <li>EM-004 : Contribution exceptionnelle sur les hauts revenus, par tranche et par statut (célibataire ou couple)</li>
 * </ul>
 *
 * <p>Le calcul s'effectue par tranches, selon un barème progressif différent selon que le foyer est composé
 * d'une part (célibataire) ou plus (couple).</p>
 */
public class CalculContributionExceptionnelle {

    /** Montant final de la CEHR arrondi à l'euro le plus proche. */
    private double contributionExceptionnelle;

    /**
     * Calcule la contribution exceptionnelle (CEHR) en appliquant un barème progressif.
     *
     * <p>Le barème est défini par tranches avec des taux différents selon le statut du foyer :</p>
     * <ul>
     *   <li>Un taux s'applique aux célibataires (1 part fiscale)</li>
     *   <li>Un autre taux s'applique aux couples (≥ 2 parts fiscales)</li>
     * </ul>
     *
     * @param revenuFiscalRef Revenu fiscal de référence du foyer
     * @param nbPartsDeclarant Nombre de parts fiscales du déclarant (1 si célibataire, >1 si couple)
     */
    public void calculerContribution(double revenuFiscalRef, double nbPartsDeclarant) {
        contributionExceptionnelle = 0;
        int i = 0;

        // Traitement par tranche de revenu, jusqu'à la 5e (index max = 4)
        do {
            if (revenuFiscalRef >= Constante.TRANCHES_CEHR[i] && revenuFiscalRef < Constante.TRANCHES_CEHR[i + 1]) {
                // Cas où le revenu se situe dans la tranche courante
                if (nbPartsDeclarant == 1) {
                    contributionExceptionnelle += (revenuFiscalRef - Constante.TRANCHES_CEHR[i]) * Constante.TAUX_CEHR_CELIBATAIRE[i];
                } else {
                    contributionExceptionnelle += (revenuFiscalRef - Constante.TRANCHES_CEHR[i]) * Constante.TAUX_CEHR_COUPLE[i];
                }
                break;
            } else {
                // Cas où la tranche entière est couverte
                if (nbPartsDeclarant == 1) {
                    contributionExceptionnelle += (Constante.TRANCHES_CEHR[i + 1] - Constante.TRANCHES_CEHR[i]) * Constante.TAUX_CEHR_CELIBATAIRE[i];
                } else {
                    contributionExceptionnelle += (Constante.TRANCHES_CEHR[i + 1] - Constante.TRANCHES_CEHR[i]) * Constante.TAUX_CEHR_COUPLE[i];
                }
            }
            i++;
        } while (i < 5);  // 5 tranches maximum

        // Arrondi final à l'euro près
        contributionExceptionnelle = Math.round(contributionExceptionnelle);
    }

    /**
     * Retourne le montant de la contribution exceptionnelle calculée.
     *
     * @return Contribution CEHR en euros
     */
    public double getContributionExceptionnelle() {
        return contributionExceptionnelle;
    }
}
