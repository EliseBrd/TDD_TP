package tdd.elise.tp.manager;

import tdd.elise.tp.models.Member;
import tdd.elise.tp.models.Reservation;
import tdd.elise.tp.service.MailService;
import tdd.elise.tp.service.ReservationDataService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationManager {
    private final ReservationDataService databaseService;

    private final ReservationDataService webService;

    private final MailService mailService;  // Déclaration de mailService

    public ReservationManager(ReservationDataService databaseService, ReservationDataService webService, MailService mailService) {
        this.databaseService = databaseService;
        this.webService = webService;
        this.mailService = mailService;
    }

    // Récupérer les réservations ouvertes
    public List<Reservation> getOpenReservations(String status) {
        return databaseService.findByStatus(status);
    }

    public List<Reservation> getReservationHistoryForMember(Member member) {
        return databaseService.findByMember(member);
    }

    public List<Reservation> getOverdueReservations(String status) {
        return databaseService.findByStatus(status);
    }

    public void sendReminderEmailsForExpiredReservations() {
        // Récupérer les réservations expirées
        List<Reservation> expiredReservations = databaseService.findByStatus("EXPIRED");

        // Regrouper les réservations par adhérent
        Map<Member, List<Reservation>> reservationsByMember = expiredReservations.stream()
                .collect(Collectors.groupingBy(Reservation::getMember));

        // Envoyer un seul email par adhérent avec la liste complète de ses réservations expirées
        for (Map.Entry<Member, List<Reservation>> entry : reservationsByMember.entrySet()) {
            mailService.sendReminderEmail(entry.getKey(), entry.getValue());
        }
    }

    public ReservationManager requestReminderEmailsForExpiredReservations() {
        return null;
    }
}
