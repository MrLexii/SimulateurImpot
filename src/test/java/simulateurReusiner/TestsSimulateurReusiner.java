package simulateurReusiner;

import com.kerware.SimulateurReusiner.AdaptateurSimuReus;
import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestsSimulateurReusiner {

    private static ICalculateurImpot simulateur;

    @BeforeAll
    public static void setUp() {
        simulateur = new AdaptateurSimuReus();
    }

    public static Stream<Arguments> donneesPartsFoyerFiscal() {
        return Stream.of(
        		Arguments.of(24000, "CELIBATAIRE", 0, 0, false, 1),
                Arguments.of(24000, "CELIBATAIRE", 1, 0, false, 1.5),
                Arguments.of(24000, "CELIBATAIRE", 2, 0, false, 2),
                Arguments.of(24000, "CELIBATAIRE", 3, 0, false, 3),
                Arguments.of(24000, "MARIE", 0, 0, false, 2),
                Arguments.of(24000, "PACSE", 0, 0, false, 2),
                Arguments.of(24000, "MARIE", 3, 1, false, 4.5),
                Arguments.of(24000, "DIVORCE", 2, 0, true, 2.5),
                Arguments.of(24000, "VEUF", 3, 0, true, 4.5),
                Arguments.of(24000, "CELIBATAIRE", 2, 1, true, 3),
                Arguments.of(24000, "PACSE", 3, 0, false, 4),
                Arguments.of(24000, "DIVORCE", 1, 1, true, 2.5),
                Arguments.of(24000, "VEUF", 2, 2, true, 4.5)
                );

    }

    // COUVERTURE EXIGENCE : EXG_IMPOT_03
    @DisplayName("Tests du calcul des parts pour différents foyers fiscaux")
    @ParameterizedTest
    @MethodSource( "donneesPartsFoyerFiscal" )
    public void testNombreDeParts( int revenuNetDeclarant1, String situationFamiliale, int nbEnfantsACharge,
                                   int nbEnfantsSituationHandicap, boolean parentIsole, double nbPartsAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( 0);
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf(situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(   nbPartsAttendu, simulateur.getNbPartsFoyerFiscal());

    }


    public static Stream<Arguments> donneesAbattementFoyerFiscal() {
        return Stream.of(
        		Arguments.of(4900, "CELIBATAIRE", 0, 0, false, 495),
                Arguments.of(12000, "CELIBATAIRE", 0, 0, false, 1200),
                Arguments.of(200000, "CELIBATAIRE", 0, 0, false, 14171),
                Arguments.of(10000, "CELIBATAIRE", 0, 0, false, 1000),
                Arguments.of(141710, "CELIBATAIRE", 0, 0, false, 14171),
                Arguments.of(4940, "CELIBATAIRE", 0, 0, false, 495),
                Arguments.of(4950, "CELIBATAIRE", 0, 0, false, 495),
                Arguments.of(4960, "CELIBATAIRE", 0, 0, false, 496),
                Arguments.of(1417100, "CELIBATAIRE", 0, 0, false, 14171),
                Arguments.of(0, "CELIBATAIRE", 0, 0, false, 495)
        );

    }

    // COUVERTURE EXIGENCE : EXG_IMPOT_03
    @DisplayName("Tests des abattements pour les foyers fiscaux")
    @ParameterizedTest
    @MethodSource( "donneesAbattementFoyerFiscal" )
    public void testAbattement( int revenuNetDeclarant1, String situationFamiliale, int nbEnfantsACharge,
                                   int nbEnfantsSituationHandicap, boolean parentIsole, int abattementAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( 0);
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf(situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(   abattementAttendu, simulateur.getAbattement());
    }


    public static Stream<Arguments> donneesRevenusFoyerFiscal() {
        return Stream.of(
        		 Arguments.of(12000, "CELIBATAIRE", 0, 0, false, 0),
                 Arguments.of(20000, "CELIBATAIRE", 0, 0, false, 199),
                 Arguments.of(35000, "CELIBATAIRE", 0, 0, false, 2736),
                 Arguments.of(95000, "CELIBATAIRE", 0, 0, false, 19284),
                 Arguments.of(200000, "CELIBATAIRE", 0, 0, false, 60768),
                 Arguments.of(25000, "CELIBATAIRE", 0, 0, false, 918),
                 Arguments.of(50000, "CELIBATAIRE", 0, 0, false, 6786),
                 Arguments.of(100000, "CELIBATAIRE", 0, 0, false, 21129),
                 Arguments.of(150000, "CELIBATAIRE", 0, 0, false, 39919),
                 Arguments.of(175000, "CELIBATAIRE", 0, 0, false, 50169),
                 Arguments.of(110000, "MARIE", 0, 0, false, 16124),
                 Arguments.of(60000, "MARIE", 0, 0, false, 3401),
                 Arguments.of(20000, "MARIE", 0, 0, false, 0),
                 Arguments.of(15000, "MARIE", 0, 0, false, 0),
                 Arguments.of(250000, "MARIE", 0, 0, false, 64944),
                 Arguments.of(12000, "MARIE", 0, 0, false, 0),
                 Arguments.of(12000, "PACSE", 0, 0, false, 0),
                 Arguments.of(12000, "VEUF", 0, 0, false, 0),
                 Arguments.of(35000, "DIVORCE", 1, 0, true, 550),
                 Arguments.of(95000, "MARIE", 2, 1, false, 6797)
        );

    }

    // COUVERTURE EXIGENCE : EXG_IMPOT_04
    @DisplayName("Tests des différents taux marginaux d'imposition")
    @ParameterizedTest
    @MethodSource( "donneesRevenusFoyerFiscal" )
    public void testTrancheImposition( int revenuNet, String situationFamiliale, int nbEnfantsACharge,
                                int nbEnfantsSituationHandicap, boolean parentIsole, int impotAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNet );
        simulateur.setRevenusNetDeclarant2( 0);
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf(situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(   impotAttendu, simulateur.getImpotSurRevenuNet());
    }



    public static Stream<Arguments> donneesRobustesse() {
        return Stream.of(
        		Arguments.of(-1, 0, "CELIBATAIRE", 0, 0, false),
                Arguments.of(20000, 0, null, 0, 0, false),
                Arguments.of(35000, 0, "CELIBATAIRE", -1, 0, false),
                Arguments.of(95000, 0, "CELIBATAIRE", 0, -1, false),
                Arguments.of(200000, 0, "CELIBATAIRE", 3, 4, false),
                Arguments.of(200000, 0, "MARIE", 3, 2, true),
                Arguments.of(200000, 0, "PACSE", 3, 2, true),
                Arguments.of(200000, 0, "MARIE", 8, 0, false),
                Arguments.of(200000, 10000, "CELIBATAIRE", 8, 0, false),
                Arguments.of(200000, 10000, "VEUF", 8, 0, false),
                Arguments.of(200000, 10000, "DIVORCE", 8, 0, false),
                Arguments.of(Integer.MAX_VALUE, 10000, "MARIE", 8, 0, false),
                Arguments.of(Integer.MIN_VALUE, 10000, "PACSE", 2, 0, false),
                Arguments.of(10000, 10000, "CELIBATAIRE", Integer.MAX_VALUE, 0, false),
                Arguments.of(10000, 10000, "CELIBATAIRE", 0, Integer.MAX_VALUE, false)
        );
    }

    // COUVERTURE EXIGENCE : Robustesse
    @DisplayName("Tests de robustesse avec des valeurs interdites")

    @ParameterizedTest( name ="Test avec revenuNetDeclarant1={0}, revenuDeclarant2={1}, situationFamiliale={2}, nbEnfantsACharge={3}, nbEnfantsSituationHandicap={4}, parentIsole={5}")
    @MethodSource( "donneesRobustesse" )
    public void testRobustesse( int revenuNetDeclarant1, int revenuNetDeclarant2 , String situationFamiliale, int nbEnfantsACharge,
                                       int nbEnfantsSituationHandicap, boolean parentIsole) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( revenuNetDeclarant2 );
        if ( situationFamiliale == null )
                simulateur.setSituationFamiliale( null  );
        else
                simulateur.setSituationFamiliale( SituationFamiliale.valueOf( situationFamiliale ));
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act & Assert
        assertThrows( IllegalArgumentException.class,  () -> { simulateur.calculImpotSurRevenuNet();} );


    }

    // AVEC D'AUTRES IDEES DE TESTS
    // AVEC @ParameterizedTest et @CsvFileSource
    @DisplayName("Tests supplémentaires de cas variés de foyers fiscaux - ")
    @ParameterizedTest( name = " avec revenuNetDeclarant1={0}, revenuNetDeclarant2={1}, situationFamiliale={2}, nbEnfantsACharge={3}, nbEnfantsSituationHandicap={4}, parentIsole={5} - IMPOT NET ATTENDU = {6}")
    @CsvFileSource( resources={"/datasImposition.csv"} , numLinesToSkip = 1 )
    public void testCasImposition( int revenuNetDeclarant1, int revenuNetDeclarant2,  String situationFamiliale, int nbEnfantsACharge,
                                       int nbEnfantsSituationHandicap, boolean parentIsole, int impotAttendu) {

       // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( revenuNetDeclarant2 );
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf( situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(   Integer.valueOf(impotAttendu), simulateur.getImpotSurRevenuNet());
    }    
}
