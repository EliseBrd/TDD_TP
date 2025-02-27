package tdd.elise.tp.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tdd.elise.tp.models.Book;
import tdd.elise.tp.models.Member;
import tdd.elise.tp.models.Reservation;
import tdd.elise.tp.models.enums.Format;
import tdd.elise.tp.models.enums.ReservationStatus;
import tdd.elise.tp.service.ReservationDataService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationManagerTest {
    @Mock
    private ReservationDataService fakeDatabaseService;

    @Mock
    private ReservationDataService fakeWebService;

    private ReservationManager reservationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationManager = new ReservationManager(fakeDatabaseService, fakeWebService);
    }

    @Test
    void shouldReturnOnlyOpenReservations() {
        // GIVEN : Adhérent et réservations mockées
        Member member = new Member("Doe", "John", new Date(), null);
        Reservation openRes1 = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() + 1000000), ReservationStatus.OPEN); // Future
        Reservation openRes2 = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() + 2000000), ReservationStatus.OPEN); // Future
        Reservation closedRes = new Reservation(null, member, new Date(), new Date(System.currentTimeMillis() - 1000000), ReservationStatus.CLOSED); // Passée

        List<Reservation> allReservations = Arrays.asList(openRes1, openRes2, closedRes);

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
}