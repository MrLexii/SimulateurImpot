package com.kerware.SimulateurReusiner;

import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;

/**
 * Adaptateur permettant d'utiliser le simulateur {@link SimulateurReusiner} 
 * avec l'interface standard {@link ICalculateurImpot}.
 *
 * <p>Ce composant assure la compatibilité avec les appels attendus 
 * dans des systèmes utilisant l'interface ICalculateurImpot, en déléguant 
 * les appels vers le moteur interne {@code SimulateurReusiner}.</p>
 *
 * <p><b>Exigences métier couvertes indirectement :</b></p>
 * <ul>
 *   <li>EM-000 à EM-005 : via délégation au simulateur Reusiner</li>
 * </ul>
 *
 * <p><b>Remarque :</b> Cette classe est un simple adaptateur et ne contient aucune logique métier propre.</p>
 */
public class AdaptateurSimuReus implements ICalculateurImpot {

    private SimulateurReusiner simulateur = new SimulateurReusiner();

    private int revenusNetDecl1 = 0;
    private int revenusNetDecl2 = 0;
    private SituationFamiliale situationFamiliale;
    private int nbEnfantsACharge;
    private int nbEnfantsSituationHandicap;
    private boolean parentIsole;

    /**
     * Définit le revenu net du déclarant 1.
     * @param rn revenu net en euros
     */
    @Override
    public void setRevenusNetDeclarant1(int rn) {
        this.revenusNetDecl1 = rn;
    }

    /**
     * Définit le revenu net du déclarant 2.
     * @param rn revenu net en euros
     */
    @Override
    public void setRevenusNetDeclarant2(int rn) {
        this.revenusNetDecl2 = rn;
    }

    /**
     * Définit la situation familiale (célibataire, marié, etc.)
     * @param sf situation familiale du foyer
     */
    @Override
    public void setSituationFamiliale(SituationFamiliale sf) {
        this.situationFamiliale = sf;
    }

    /**
     * Définit le nombre d'enfants à charge du foyer fiscal.
     * @param nbe nombre total d'enfants
     */
    @Override
    public void setNbEnfantsACharge(int nbe) {
        this.nbEnfantsACharge = nbe;
    }

    /**
     * Définit le nombre d'enfants en situation de handicap.
     * @param nbesh nombre total d'enfants handicapés
     */
    @Override
    public void setNbEnfantsSituationHandicap(int nbesh) {
        this.nbEnfantsSituationHandicap = nbesh;
    }

    /**
     * Indique si le foyer est en situation de parent isolé.
     * @param pi true si parent isolé, false sinon
     */
    @Override
    public void setParentIsole(boolean pi) {
        this.parentIsole = pi;
    }

    /**
     * Lance le calcul de l'impôt en appelant le simulateur interne.
     */
    @Override
    public void calculImpotSurRevenuNet() {
        simulateur.calculerImpot(
            revenusNetDecl1,
            revenusNetDecl2,
            situationFamiliale,
            nbEnfantsACharge,
            nbEnfantsSituationHandicap,
            parentIsole
        );
    }

    @Override
    public int getRevenuNetDeclatant1() {
        return revenusNetDecl1;
    }

    @Override
    public int getRevenuNetDeclatant2() {
        return revenusNetDecl2;
    }

    @Override
    public double getContribExceptionnelle() {
        return simulateur.getContributionExceptionnelle();
    }

    @Override
    public int getRevenuFiscalReference() {
        return (int) simulateur.getRevenuReference();
    }

    @Override
    public int getAbattement() {
        return (int) simulateur.getAbattement();
    }

    @Override
    public double getNbPartsFoyerFiscal() {
        return simulateur.getNbPartsFoyerFiscal();
    }

    @Override
    public int getImpotAvantDecote() {
        return (int) simulateur.getImpotAvantDecote();
    }

    @Override
    public int getDecote() {
        return (int) simulateur.getDecote();
    }

    @Override
    public int getImpotSurRevenuNet() {
        return (int) simulateur.getImpotNet();
    }
}
