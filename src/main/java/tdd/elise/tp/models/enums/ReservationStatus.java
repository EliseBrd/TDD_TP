package tdd.elise.tp.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public enum ReservationStatus {
    OPEN("Réservation ouvert"),
    CLOSED("Réservation fermée"),
    PENDING("Réservation en attente de confirmation"),
    EXPIRED("Réservation expirée");

    private final String name;

    ReservationStatus(String name) { // Constructeur explicite
        this.name = name;
    }
}
