package tdd.elise.tp.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tdd.elise.tp.exceptions.InvalidBirthdateException;
import tdd.elise.tp.exceptions.InvalidEmailException;
import tdd.elise.tp.models.Member;
import tdd.elise.tp.service.BookDataService;
import tdd.elise.tp.service.MemberDataService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MemberManagerTest {
    @Mock
    private MemberDataService fakeDatabaseService;

    @Mock
    private MemberDataService fakeWebService;

    private MemberManager memberManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialisation correcte de MemberManager et non de BookManager
        memberManager = new MemberManager(fakeDatabaseService, fakeWebService);
    }

    @Test
    public void whenBirthdateIsInTheFuture_shouldThrowInvalidBirthdateException() {
        // GIVEN un membre avec une date de naissance dans le futur
        Date futureDate = new Date(System.currentTimeMillis() + 1000000000); // Date dans le futur
        Member memberWithFutureBirthdate = new Member("Doe", "John", futureDate, null, "john.doe@email.com");

        // WHEN tentative de validation de la date de naissance
        // THEN vérifier que l'exception est lancée
        assertThrows(InvalidBirthdateException.class, () -> {
            memberManager.validateBirthdate(memberWithFutureBirthdate);
        });
    }

    @Test
    public void whenBirthdateIsValid_shouldNotThrowException() {
        // GIVEN un membre avec une date de naissance valide (dans le passé)
        Date validDate = new Date(System.currentTimeMillis() - 1000000000); // Date valide dans le passé
        Member memberWithValidBirthdate = new Member("Doe", "John", validDate, null, "john.doe@email.com");

        // WHEN tentative de validation de la date de naissance
        // THEN vérifier qu'aucune exception n'est lancée
        assertDoesNotThrow(() -> {
            memberManager.validateBirthdate(memberWithValidBirthdate);
        });
    }

    @Test
    public void whenEmailIsValid_shouldNotThrowException() {
        // GIVEN un membre avec un email valide
        Member validMember = new Member("Doe", "John", new Date(), null, "john.doe@email.com");

        // WHEN tentative de validation de l'email
        // THEN vérifier qu'aucune exception n'est lancée
        assertDoesNotThrow(() -> {
            memberManager.validateEmail(validMember);
        });
    }

    @Test
    public void whenEmailIsInvalid_shouldThrowInvalidEmailException() {
        // GIVEN un membre avec un email invalide
        Member invalidMember = new Member("Doe", "John", new Date(), null, "jane.doeemail.com");

        // WHEN tentative de validation de l'email
        // THEN vérifier que l'exception est lancée
        assertThrows(InvalidEmailException.class, () -> {
            memberManager.validateEmail(invalidMember);
        });
    }

}