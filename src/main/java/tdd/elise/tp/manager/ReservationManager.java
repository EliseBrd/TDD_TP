package tdd.elise.tp.manager;

import tdd.elise.tp.models.Reservation;
import tdd.elise.tp.service.ReservationDataService;

import java.util.List;

public class ReservationManager {
    private final ReservationDataService databaseService;

    private final ReservationDataService webService;

    public ReservationManager(ReservationDataService databaseService, ReservationDataService webService) {
        this.databaseService = databaseService;
        this.webService = webService;
    }

    // Récupérer les réservations ouvertes
    public List<Reservation> getOpenReservations() {
        return null;
    }

}
