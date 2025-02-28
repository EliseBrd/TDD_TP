package tdd.elise.tp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import tdd.elise.tp.models.Member;
import tdd.elise.tp.models.Reservation;

import java.util.List;

@Service
public class MailService {

    private JavaMailSender mailSender;

    public void sendReminderEmail(Member member, List<Reservation> reservations) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());
        message.setSubject("Rappel de vos réservations expirées");

        StringBuilder emailBody = new StringBuilder("Vos réservations suivantes sont expirées :\n");
        for (Reservation res : reservations) {
            emailBody.append("- Réservation du ").append(res.getStartDate()).append("\n");
        }

        message.setText(emailBody.toString());
        mailSender.send(message);  // Envoi de l'email
    }

    public void requestReminderEmail(Member member, List<Reservation> expiredReservations) {
        // Implémentation de l'envoi d'un email de rappel
        System.out.println("Envoi d'un email de rappel à " + member.getEmail() + " pour " + expiredReservations.size() + " réservations expirées.");
    }

}
