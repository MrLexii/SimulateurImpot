package com.kerware.SimulateurReusiner;

import com.kerware.simulateur.SituationFamiliale;

/**
 * Cette classe calcule le nombre de parts fiscales d’un foyer selon la législation française.
 *
 * <p><b>Exigences couvertes :</b></p>
 * <ul>
 *   <li>METIER-003 : Calcul des parts fiscales selon la situation familiale, enfants, handicap, parent isolé</li>
 *   <li>EM-004 : Application du système des demi-parts pour enfants, parts supplémentaires pour enfants handicapés</li>
 *   <li>EM-005 : Prise en compte des cas spécifiques (veuf avec enfants, parent isolé)</li>
 * </ul>
 */
public class CalculPartsFiscales {

    private double nbPartsFoyerFiscal;  // Total des parts fiscales (déclarant + enfants + bonus)
    private double nbPartsDeclarant;    // Parts de base liées au statut du foyer (1 ou 2)

    /**
     * Calcule le nombre total de parts fiscales d’un foyer.
     *
     * @param nbEnfants Nombre d’enfants à charge
     * @param situation Situation familiale (célibataire, marié, etc.)
     * @param parentIsole Vrai si le parent est isolé (éducation d’enfants seul(e))
     * @param nbEnfantsHandicap Nombre d’enfants à charge en situation de handicap
     */
    public void calculerParts(int nbEnfants, SituationFamiliale situation, boolean parentIsole,
                              int nbEnfantsHandicap) {
        // Initialisation des parts de base selon la situation matrimoniale
        switch (situation) {
            case CELIBATAIRE:
            case DIVORCE:
            case VEUF:
                nbPartsDeclarant = 1;
                break;
            case MARIE:
            case PACSE:
                nbPartsDeclarant = 2;
                break;
            default:
                throw new IllegalArgumentException("Situation familiale inconnue");
        }

        // Ajout des parts liées aux enfants
        if (nbEnfants <= 2) {
            nbPartsFoyerFiscal = nbPartsDeclarant + nbEnfants * 0.5;
        } else {
            // À partir du 3e enfant : une part entière par enfant
            nbPartsFoyerFiscal = nbPartsDeclarant + 1.0 + (nbEnfants - 2);
        }

        // Bonus parent isolé (0.5 part en plus si au moins un enfant)
        if (parentIsole && nbEnfants > 0) {
            nbPartsFoyerFiscal += 0.5;
        }

        // Veuf avec enfant à charge : part entière supplémentaire
        if (situation == SituationFamiliale.VEUF && nbEnfants > 0) {
            nbPartsFoyerFiscal += 1;
        }

        // Bonus pour enfant handicapé (0.5 part par enfant handicapé)
        nbPartsFoyerFiscal += nbEnfantsHandicap * 0.5;
    }

    /**
     * @return Le nombre total de parts fiscales calculé pour le foyer.
     */
    public double getPartsFoyerFiscal() {
        return nbPartsFoyerFiscal;
    }

    /**
     * @return Le nombre de parts du ou des déclarants sans les enfants (1 ou 2 selon situation).
     */
    public double getPartsDeclarant() {
        return nbPartsDeclarant;
    }
}
