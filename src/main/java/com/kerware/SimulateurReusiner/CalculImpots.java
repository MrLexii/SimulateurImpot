package com.kerware.SimulateurReusiner;

public class CalculImpots {
    private double revenuFiscalRef;
    private double partsContribuable;
    private double partsFiscales;

    public CalculImpots(double revenuFiscalReference, double nbPartsDeclarant, double impotFoyerFiscal) {
		this.revenuFiscalRef = revenuFiscalReference;
		this.partsContribuable = nbPartsDeclarant;
		this.partsFiscales = impotFoyerFiscal;
	}

	/**
     * Calcule l'impôt brut en fonction du revenu fiscal de référence et des tranches d'imposition.
     * @return Le montant de l'impôt brut calculé.
     */
    public double calculImpôtAvantPlafond() {
    	double rImposable = revenuFiscalRef / partsContribuable;
        double mImpDecl = 0;
        int i = 0;
        do {
            if (rImposable >= Constante.TRANCHES_IMPOSITION[i] && rImposable < Constante.TRANCHES_IMPOSITION[i + 1]) {
                mImpDecl += (rImposable - Constante.TRANCHES_IMPOSITION[i]) * Constante.TAUX_TRANCHES[i];
                break;
            } else {
                mImpDecl += (Constante.TRANCHES_IMPOSITION[i + 1] - Constante.TRANCHES_IMPOSITION[i]) * Constante.TAUX_TRANCHES[i];
            }
            i++;
        } while (i < Constante.TRANCHES_IMPOSITION.length - 1);
        mImpDecl *= partsContribuable;
        mImpDecl = Math.round(mImpDecl);
        return mImpDecl;
    }

    /**
     * Calcule l'impôt final en appliquant le quotient familial, les plafonds et les ajustements.
     * @return Le montant de l'impôt final calculé.
     EXIGENCE : L’impôt doit être calculé par part fiscale en tenant compte des tranches d’imposition et du quotient familial.
     */
    public double calculImpôtFoyer() {
    	double rffImposable = revenuFiscalRef / partsFiscales;
        double ffmImp = 0;
        int i = 0;
        do {
            if (rffImposable >= Constante.TRANCHES_IMPOSITION[i] && rffImposable < Constante.TRANCHES_IMPOSITION[i + 1]) {
                ffmImp += (rffImposable - Constante.TRANCHES_IMPOSITION[i]) * Constante.TAUX_TRANCHES[i];
                break;
            } else {
                ffmImp += (Constante.TRANCHES_IMPOSITION[i + 1] - Constante.TRANCHES_IMPOSITION[i]) * Constante.TAUX_TRANCHES[i];
            }
            i++;
        } while (i < Constante.TRANCHES_IMPOSITION.length - 1);
        ffmImp *= partsFiscales;
        ffmImp = Math.round(ffmImp);
        return ffmImp;
    }
}
