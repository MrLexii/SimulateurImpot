package com.kerware.SimulateurReusiner;

import com.kerware.simulateur.SituationFamiliale;

public class CalculPartsFiscales {
	
    private double nbPartsFoyerFiscal;
    private double nbPartsDeclarant;

	/**
     * Calcule le nombre de parts fiscales du contribuable en fonction de la situation familiale,
     * du nombre d'enfants et de la situation d'isolement.
     * @param nbEnfants Nombre d'enfants à charge.
     * @param situation Situation familiale du contribuable (Célibataire, Marié, etc.).
     * @param parentIsole Indicateur si le parent est isolé (ne partage pas les parts avec un conjoint).
     * @param nbEnfantsHandicap Nombre d'enfants handicapés à charge.
     */
    public void calculerParts(int nbEnfants, SituationFamiliale situation, boolean parentIsole, int nbEnfantsHandicap) {
    	switch ( situation ) {
        case CELIBATAIRE:
        	nbPartsDeclarant = 1;
            break;
        case MARIE:
        	nbPartsDeclarant = 2;
            break;
        case DIVORCE:
        	nbPartsDeclarant = 1;
            break;
        case VEUF:
        	nbPartsDeclarant = 1;
            break;
        case PACSE:
        	nbPartsDeclarant = 2;
            break;
	    }
	
	    // parts enfants à charge
	    if ( nbEnfants <= 2 ) {
	    	nbPartsFoyerFiscal = nbPartsDeclarant + nbEnfants * 0.5;
	    } else if ( nbEnfants > 2 ) {
	    	nbPartsFoyerFiscal = nbPartsDeclarant+  1.0 + ( nbEnfants - 2 );
	    }
	
	    // parent isolé
	    if ( parentIsole ) {
	        if ( nbEnfants > 0 ){
	        	nbPartsFoyerFiscal = nbPartsFoyerFiscal + 0.5;
	        }
	    }
	
	    // Veuf avec enfant
	    if ( situation == SituationFamiliale.VEUF && nbEnfants > 0 ) {
	    	nbPartsFoyerFiscal = nbPartsFoyerFiscal + 1;
	    }
	
	    // enfant handicapé
	    nbPartsFoyerFiscal = nbPartsFoyerFiscal + nbEnfantsHandicap * 0.5;

    }

    /**
     * Getter pour obtenir le nombre total de parts fiscales.
     * @return Le nombre total de parts fiscales.
     */
    public double getPartsFoyerFiscal() {
        return nbPartsFoyerFiscal;
    }

    /**
     * Getter pour obtenir le nombre de parts du contribuable (avant les enfants).
     * @return Le nombre de parts du contribuable.
     */
    public double getPartsDeclarant() {
        return nbPartsDeclarant;
    }
}
