package tdd.elise.tp.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tdd.elise.tp.models.Book;
import tdd.elise.tp.models.Member;
import tdd.elise.tp.models.Reservation;
import tdd.elise.tp.models.enums.Civility;
import tdd.elise.tp.models.enums.Format;
import tdd.elise.tp.models.enums.ReservationStatus;
import tdd.elise.tp.service.BookDataService;
import tdd.elise.tp.service.MailService;
import tdd.elise.tp.service.MemberDataService;
import tdd.elise.tp.service.ReservationDataService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationManagerTest {
    @Mock
    private ReservationDataService fakeDatabaseService;

    @Mock
    private ReservationDataService fakeWebService;

    @Mock
    private MailService mockMailService;

    @Mock
    private BookDataService bookDataService;

    @Mock
    private MemberDataService memberDataService;

    @InjectMocks
    private ReservationManager reservationManager;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationManager = new ReservationManager(fakeDatabaseService, fakeWebService, mockMailService, memberDataService, bookDataService);

    }
    /* -------------------------------------------------------
     *                      RESERVATIONS
     * -------------------------------------------------------*/
    @Test
    void shouldRequestReservationForMember() {
        // GIVEN : Un adhérent et un livre existant
        String memberId = "123";
        String ISBN = "2253009687";
        String TITLE = "Notre Dame de Paris";
        String AUTHOR = "Victor Hugo";
        String EDITOR = "Guillaume";

        // Mock d'un livre
        Book mockBook = new Book(ISBN, TITLE, AUTHOR, EDITOR, Format.PAPERBACK, true);

        // Mock d'un membre
        Member mockMember = new Member("Doe", "John", new Date(), null, "john.doe@email.com");


        // Simuler les retours des services mockés
        when(bookDataService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(memberDataService.findById(memberId)).thenReturn(mockMember);
        when(fakeDatabaseService.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN : On demande une réservation
        Reservation reservation = reservationManager.requestReservation(memberId, ISBN);

        // THEN : Vérification des données de la réservation
        assertEquals(mockBook, reservation.getBook());
        assertEquals(mockMember, reservation.getMember());
        assertEquals(ReservationStatus.PENDING, reservation.getStatus());

        // Vérifie que `save` a bien été appelé
        verify(fakeDatabaseService, times(1)).save(any(Reservation.class));
    }

    // Une réservation a une date limite, d’un maximum de 4 mois au jour du dépôt de la réservation.
    @Test
    void shouldNotAllowReservationExceeding4MonthsLimit() {

    }

    // On peut mettre fin à la réservation
    @Test
    void shouldAllowEndingReservation() {

    }

    // Un adhérent ne peut avoir plus de 3 réservations ouvertes simultanées.
    @Test
    void shouldNotAllowMoreThanThreeOpenReservationsForMember() {

    }


    /* -------------------------------------------------------
     *                      GESTION
     * -------------------------------------------------------*/

    @Test
    void shouldReturnOnlyOpenReservations() {
        // GIVEN : Adhérent et réservations mockées
        Member member = new Member("Doe", "John", new Date(), null, "john.doe@email.com");
        Reservation openRes1 = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() + 1000000), ReservationStatus.OPEN); // Future
        Reservation openRes2 = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() + 2000000), ReservationStatus.OPEN); // Future
        Reservation closedRes = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() - 1000000), ReservationStatus.CLOSED); // Passée

        // Mock du service de base de données
        when(fakeDatabaseService.findByStatus("OPEN")).thenReturn(Arrays.asList(openRes1, openRes2)); // Seulement les réservations ouvertes

        // WHEN : Appel de la méthode testée
        List<Reservation> openReservations = reservationManager.getOpenReservations("OPEN");

        // Vérification des résultats
        assertEquals(2, openReservations.size()); // On attend 2 réservations ouvertes
        assertEquals(openRes1, openReservations.get(0));
        assertEquals(openRes2, openReservations.get(1));

        // Vérifie que le service a bien été appelé
        verify(fakeDatabaseService, times(1)).findByStatus("OPEN");
    }

    @Test
    void shouldReturnReservationsHistoryForMember() {
        // GIVEN : Un adhérent et plusieurs réservations (passées et ouvertes)
        Member member = new Member("Doe", "John", new Date(), Civility.MR, "john.doe@email.com");

        Reservation pastReservation = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() - 5000000), ReservationStatus.CLOSED); // Passée
        Reservation openReservation = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() + 5000000), ReservationStatus.OPEN); // Future

        List<Reservation> historyReservations = Arrays.asList(pastReservation, openReservation);

        // Mock du service pour retourner les réservations de l’adhérent
        when(fakeDatabaseService.findByMember(member)).thenReturn(historyReservations);

        // WHEN : Appel de la méthode testée
        List<Reservation> reservationHistory = reservationManager.getReservationHistoryForMember(member);

        // THEN : Vérifications
        assertEquals(2, reservationHistory.size()); // On attend 2 réservations
        assertTrue(reservationHistory.contains(pastReservation));
        assertTrue(reservationHistory.contains(openReservation));

        // Vérifie que le service a bien été appelé
        verify(fakeDatabaseService, times(1)).findByMember(member);
    }

    @Test
    void shouldRequestReminderEmailsForExpiredReservations() {
        // GIVEN : Un adhérent avec deux réservations expirées
        Member member = new Member("Doe", "John", new Date(), Civility.MR, "john.doe@email.com");

        Reservation expiredRes1 = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() - 1000000), ReservationStatus.EXPIRED);
        Reservation expiredRes2 = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() - 2000000), ReservationStatus.EXPIRED);

        List<Reservation> expiredReservations = List.of(expiredRes1, expiredRes2);

        when(fakeDatabaseService.findByStatus("EXPIRED")).thenReturn(expiredReservations);

        // WHEN : On appelle la méthode qui demande l'envoi des emails
        reservationManager.requestReminderEmailsForExpiredReservations();

        // THEN : Vérifier que `requestReminderEmail` est appelé une seule fois avec la liste complète des réservations pour ce membre
        verify(mockMailService, times(1)).requestReminderEmail(eq(member), argThat((List<Reservation> resList) -> resList.size() == 2));
    }

    @Test
    void shouldSendOnlyOneEmailPerMemberForMultipleExpiredReservations() {
        // GIVEN : Un adhérent avec deux réservations expirées
        Member member = new Member("Doe", "John", new Date(), Civility.MR, "john.doe@email.com");

        Reservation expiredRes1 = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() - 1000000), ReservationStatus.EXPIRED);
        Reservation expiredRes2 = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() - 2000000), ReservationStatus.EXPIRED);

        List<Reservation> expiredReservations = List.of(expiredRes1, expiredRes2);

        when(fakeDatabaseService.findByStatus("EXPIRED")).thenReturn(expiredReservations);

        // WHEN : On appelle la méthode qui envoie les emails
        reservationManager.sendReminderEmailsForExpiredReservations();

        // THEN : Vérifier que sendReminderEmail est appelé une seule fois avec la liste complète des réservations
        verify(mockMailService, times(1)).sendReminderEmail(eq(member), argThat(resList -> resList.size() == 2));
    }

    @Test
    void shouldReturnEmptyListWhenNoExpiredReservations() {
        // GIVEN : Aucune réservation expirée
        when(fakeDatabaseService.findByStatus("EXPIRED")).thenReturn(emptyList());

        // WHEN : Appel de la méthode testée
        List<Reservation> overdueReservations = reservationManager.getOverdueReservations("EXPIRED");

        // THEN : La liste doit être vide
        assertTrue(overdueReservations.isEmpty());

        // Vérifier que le service a bien été appelé
        verify(fakeDatabaseService, times(1)).findByStatus("EXPIRED");
    }
}