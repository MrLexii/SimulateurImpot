package com.kerware.SimulateurReusiner;

import com.kerware.simulateur.SituationFamiliale;

public class CalculPartsFiscales {
	
    private double partsFiscales;
    private double partsContribuable;

	/**
     * Calcule le nombre de parts fiscales du contribuable en fonction de la situation familiale,
     * du nombre d'enfants et de la situation d'isolement.
     * @param nbEnfants Nombre d'enfants à charge.
     * @param situation Situation familiale du contribuable (Célibataire, Marié, etc.).
     * @param parentIsole Indicateur si le parent est isolé (ne partage pas les parts avec un conjoint).
     * @param nbEnfantsHandicap Nombre d'enfants handicapés à charge.
     */
    public void calculerParts(int nbEnfants, SituationFamiliale situation, boolean parentIsole, int nbEnfantsHandicap) {
    	// Base : 1 part ou 2 parts selon la situation
    	switch ( situation ) {
        case CELIBATAIRE:
        	partsContribuable = 1;
            break;
        case MARIE:
        	partsContribuable = 2;
            break;
        case DIVORCE:
        	partsContribuable = 1;
            break;
        case VEUF:
        	partsContribuable = 1;
            break;
        case PACSE:
        	partsContribuable = 2;
            break;
    }

        // Ajout des parts liées aux enfants
        if (nbEnfants <= 2) {
            partsFiscales = partsContribuable + nbEnfants * 0.5;
        } else {
            partsFiscales = partsContribuable + 1.0 + (nbEnfants - 2);
        }

        // Majoration pour parent isolé avec enfant
        if (parentIsole && nbEnfants > 0) {
            partsFiscales += 0.5;
        }

        // Majoration pour veuf(ve) avec enfant
        if (situation == SituationFamiliale.VEUF && nbEnfants > 0) {
            partsFiscales += 1.0;
        }

        // Majoration pour enfants handicapés
        partsFiscales += nbEnfantsHandicap * 0.5;
    }

    /**
     * Getter pour obtenir le nombre total de parts fiscales.
     * @return Le nombre total de parts fiscales.
     */
    public double getPartsFiscales() {
        return partsFiscales;
    }

    /**
     * Getter pour obtenir le nombre de parts du contribuable (avant les enfants).
     * @return Le nombre de parts du contribuable.
     */
    public double getPartsContribuable() {
        return partsContribuable;
    }
}
