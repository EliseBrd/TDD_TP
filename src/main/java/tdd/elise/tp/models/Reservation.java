package tdd.elise.tp.models;

import jakarta.persistence.ManyToOne;

import java.util.Date;

public class Reservation {
    @ManyToOne
    private Book book;

    private Member member;

    private Date startDate;

    private Date endDate;
}
