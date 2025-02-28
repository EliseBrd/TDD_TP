package tdd.elise.tp.service;

import org.springframework.stereotype.Service;
import tdd.elise.tp.models.Book;
import tdd.elise.tp.models.Member;
import tdd.elise.tp.models.Reservation;

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
}
