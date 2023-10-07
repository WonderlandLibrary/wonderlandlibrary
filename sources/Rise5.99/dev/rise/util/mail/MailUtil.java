package dev.rise.util.mail;

import lombok.experimental.UtilityClass;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//By Alan

@UtilityClass
public class MailUtil {

    public void sendEmail(final String subject, final String content) {
        final Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        final String accountEmail = "loginformationrise@gmail.com";
        final String accountPassword = "hsgfhdfghdgfh676578";

        final Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(accountEmail, accountPassword);
            }
        });

        final Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(accountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(accountEmail));
            message.setSubject(subject);
            message.setText(content);
        } catch (final MessagingException e) {
            e.printStackTrace();
        }

        try {
            Transport.send(message);
        } catch (final MessagingException e) {
            e.printStackTrace();
        }
    }
}
