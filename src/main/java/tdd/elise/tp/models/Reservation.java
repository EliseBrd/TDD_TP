package tdd.elise.tp.models;

import jakarta.persistence.*;
import lombok.*;
import tdd.elise.tp.models.enums.ReservationStatus;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends PersistentEntity {

    @ManyToOne
    private Book book;

    @ManyToOne
    private Member member;

    private Date startDate;

    private Date endDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    // Méthode pour fermer la réservation
    public void endReservation() {
        this.status = ReservationStatus.CLOSED;
    }
}
