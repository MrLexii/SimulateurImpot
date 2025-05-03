package com.kerware.SimulateurReusiner;

import com.kerware.simulateur.SituationFamiliale;

/**
 * Cette classe calcule l'abattement de 10% appliqué aux revenus nets des déclarants,
 * conformément aux règles fiscales en vigueur.
 *
 * <p><b>Exigences couvertes :</b></p>
 * <ul>
 *   <li>METIER-001 : Respect des règles fiscales 2024 (base DGFiP)</li>
 *   <li>EM-001 : Application d'un abattement de 10% sur les revenus nets, avec un minimum et un maximum définis</li>
 * </ul>
 *
 * <p>L'abattement est appliqué à chaque déclarant. Si le contribuable est marié ou pacsé,
 * les deux revenus sont concernés ; sinon, seul le revenu principal est pris en compte.</p>
 */
public class CalculAbattement {

    // Variables pour le calcul de l'abattement
    private double abattement;
    private double abattement1;
    private double abattement2;
    private double revenuFiscalRef;

    /**
     * Calcule l'abattement fiscal de 10% sur les revenus nets.
     *
     * <p>Les règles sont les suivantes :</p>
     * <ul>
     *   <li>Un abattement de 10% est appliqué sur chaque revenu</li>
     *   <li>L'abattement est plafonné entre 495 € et 14 171 € par personne</li>
     *   <li>Pour les célibataires, seul le revenu principal est pris en compte</li>
     * </ul>
     *
     * @param revenu1 Revenu net du déclarant 1
     * @param revenu2 Revenu net du déclarant 2 (0 si non concerné)
     * @param situation Situation familiale (célibataire, marié, pacsé)
     */
    public void calculerAbattement(int revenu1, int revenu2, SituationFamiliale situation) {
        // Abattement de 10% sur les deux revenus
        abattement1 = Math.round(revenu1 * Constante.TAUX_ABATTEMENT);
        abattement2 = Math.round(revenu2 * Constante.TAUX_ABATTEMENT);

        // Plafonnement de l'abattement dans les bornes autorisées
        abattement1 = Math.min(Math.max(abattement1, Constante.ABATTEMENT_MIN), Constante.ABATTEMENT_MAX);

        // Seuls les couples mariés ou pacsés ont droit à un second abattement
        if (situation == SituationFamiliale.MARIE || situation == SituationFamiliale.PACSE) {
            abattement2 = Math.min(Math.max(abattement2, Constante.ABATTEMENT_MIN), Constante.ABATTEMENT_MAX);
        } else {
            abattement2 = 0;
        }

        abattement = abattement1 + abattement2;

        // Calcul du revenu fiscal de référence après abattement
        revenuFiscalRef = Math.max(0, revenu1 + revenu2 - abattement);
    }

    /**
     * Retourne le montant total de l'abattement appliqué.
     *
     * @return Abattement cumulé (déclarant 1 + déclarant 2)
     */
    public double getAbattement() {
        return abattement;
    }

    /**
     * Retourne le revenu fiscal de référence (RFR) après application de l’abattement.
     *
     * @return RFR calculé
     */
    public double getRevenuFiscalRef() {
        return revenuFiscalRef;
    }
}
