package tdd.elise.tp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import tdd.elise.tp.models.Reservation;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReminderEmail(Reservation reservation) {

    }
}
