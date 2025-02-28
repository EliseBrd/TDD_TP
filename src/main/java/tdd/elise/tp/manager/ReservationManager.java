package tdd.elise.tp.manager;

import tdd.elise.tp.models.Book;
import tdd.elise.tp.models.Member;
import tdd.elise.tp.models.Reservation;
import tdd.elise.tp.service.BookDataService;
import tdd.elise.tp.service.MailService;
import tdd.elise.tp.service.ReservationDataService;
import tdd.elise.tp.service.MemberDataService;
import tdd.elise.tp.models.enums.ReservationStatus;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationManager {
    private final ReservationDataService databaseService;

    private final ReservationDataService webService;

    private final MemberDataService memberDataService;

    private final BookDataService bookDataService;

    private final MailService mailService;  // Déclaration de mailService

    public ReservationManager(ReservationDataService databaseService, ReservationDataService webService, MailService mailService, MemberDataService memberDataService, BookDataService bookDataService) {
        this.databaseService = databaseService;
        this.webService = webService;
        this.mailService = mailService;
        this.memberDataService = memberDataService;
        this.bookDataService = bookDataService;
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

    public void requestReminderEmailsForExpiredReservations() {
        // Regrouper les réservations par membre
        Map<Member, List<Reservation>> reservationsByMember = databaseService.findByStatus("EXPIRED")
                .stream()
                .collect(Collectors.groupingBy(Reservation::getMember));

        // Demander l'envoi d'un mail pour chaque membre
        reservationsByMember.forEach((member, reservations) -> {
            mailService.requestReminderEmail(member, reservations);
        });
    }

    public Reservation requestReservation(String memberId, String ISBN) {
        return null;
    }
}
