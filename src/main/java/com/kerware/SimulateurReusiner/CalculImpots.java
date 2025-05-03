package com.kerware.SimulateurReusiner;

/**
 * Cette classe effectue les calculs de l'impôt brut et de l'impôt net en fonction du barème fiscal 2024.
 * Elle applique la logique par tranches, en tenant compte du quotient familial.
 *
 * <p><b>Exigences couvertes :</b></p>
 * <ul>
 *   <li>METIER-001 : Respect des règles fiscales 2024 (base DGFiP)</li>
 *   <li>METIER-002 : Prise en compte du quotient familial et du barème progressif</li>
 *   <li>EM-003 : Calcul de l'impôt avant et après quotient familial</li>
 * </ul>
 */
public class CalculImpots {

    /**
     * Calcule l'impôt sans quotient familial (cas de référence, utilisé pour le plafonnement du quotient familial).
     *
     * <p>Ce calcul applique le barème progressif de l'impôt sur le revenu à un revenu ramené à une part fiscale (célibataire).</p>
     *
     * @param revenu Revenu fiscal de référence
     * @param nbPartsDeclarant Nombre de parts du déclarant (souvent 1)
     * @return Impôt brut sans quotient familial
     */
    public double calculImpôtAvantPlafond(double revenu, double nbPartsDeclarant) {
        double rImposable = revenu / nbPartsDeclarant;
        double mImpDecl = 0;
        int i = 0;

        // Application du barème par tranche
        do {
            if (rImposable >= Constante.TRANCHES_IMPOSITION[i] && rImposable < Constante.TRANCHES_IMPOSITION[i + 1]) {
                mImpDecl += (rImposable - Constante.TRANCHES_IMPOSITION[i]) * Constante.TAUX_TRANCHES[i];
                break;
            } else {
                mImpDecl += (Constante.TRANCHES_IMPOSITION[i + 1] - Constante.TRANCHES_IMPOSITION[i]) * Constante.TAUX_TRANCHES[i];
            }
            i++;
        } while (i < Constante.TRANCHES_IMPOSITION.length - 1);

        mImpDecl *= nbPartsDeclarant;  // Recalibrage à la situation réelle
        return Math.round(mImpDecl);
    }

    /**
     * Calcule l'impôt réel du foyer fiscal en tenant compte du nombre total de parts (quotient familial).
     *
     * <p>Ce calcul est utilisé pour le montant d'impôt à payer avant la décote ou tout plafonnement.</p>
     *
     * @param revenu Revenu fiscal de référence
     * @param partsFoyer Nombre total de parts fiscales (inclut enfants, parent isolé, etc.)
     * @return Impôt du foyer calculé avec le quotient familial
     */
    public double calculImpôtFoyer(double revenu, double partsFoyer) {
        double rffImposable = revenu / partsFoyer;
        double ffmImp = 0;
        int i = 0;

        // Barème progressif appliqué à chaque tranche
        do {
            if (rffImposable >= Constante.TRANCHES_IMPOSITION[i] && rffImposable < Constante.TRANCHES_IMPOSITION[i + 1]) {
                ffmImp += (rffImposable - Constante.TRANCHES_IMPOSITION[i]) * Constante.TAUX_TRANCHES[i];
                break;
            } else {
                ffmImp += (Constante.TRANCHES_IMPOSITION[i + 1] - Constante.TRANCHES_IMPOSITION[i]) * Constante.TAUX_TRANCHES[i];
            }
            i++;
        } while (i < Constante.TRANCHES_IMPOSITION.length - 1);

        ffmImp *= partsFoyer;  // Application finale du quotient familial
        return Math.round(ffmImp);
    }
}
