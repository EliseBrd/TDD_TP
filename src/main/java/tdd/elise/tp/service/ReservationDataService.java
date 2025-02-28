package tdd.elise.tp.service;

import org.springframework.stereotype.Service;
import tdd.elise.tp.models.Book;
import tdd.elise.tp.models.Member;
import tdd.elise.tp.models.Reservation;
import tdd.elise.tp.models.enums.ReservationStatus;

import java.util.Date;
import java.util.List;

@Service
public class ReservationDataService {
    public List<Reservation> getAllReservations() {
        return null;
    }

    public List<Reservation> findByStatus(String status) {
        return null;
    }

    public List<Reservation> findByMember(Member member) {
        return null; // Simule une méthode qui retourne des réservations
    }

    public Reservation save(Reservation reservation) {
        return null;
    }

    public List<Reservation> findByMemberAndStatus(Member member, ReservationStatus status) {
        return null;
    }

    public List<Reservation> getReservationsForMember(Member member, ReservationStatus reservationStatus) {
        return null;
    }
}
