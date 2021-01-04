package pl.programowaniezespolowe.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.programowaniezespolowe.planner.user.MailUser;

@Service
public class NotificationService
{
    private JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender)
    {
        super();
        this.javaMailSender = javaMailSender;
    }

    public void sendNotification(MailUser mailUser) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(mailUser.getEmailAddress());
        mail.setFrom("planner.testowy@gmail.com");
        mail.setSubject("Witamy w aplikacji Planner!!1");
        mail.setText("Cześć "+mailUser.getName()+"!\n Od teraz możesz korzystać z aplikacji Planner. Przejdz do sekcji logowania");
        javaMailSender.send(mail);
    }
}
