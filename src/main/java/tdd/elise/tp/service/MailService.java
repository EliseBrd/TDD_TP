package tdd.elise.tp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import tdd.elise.tp.models.Reservation;

@Service
public class MailService {

    private JavaMailSender mailSender;

    public void sendReminderEmail(Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(reservation.getMember().getEmail());
        message.setSubject("Rappel de votre réservation expirée");
        message.setText("Votre réservation est expirée. Merci de la régulariser au plus vite.");
        mailSender.send(message);  // Envoi de l'email
    }
}
