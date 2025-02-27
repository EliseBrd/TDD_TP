package tdd.elise.tp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tdd.elise.tp.models.enums.ReservationStatus;

import java.util.Date;

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

}
