import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {
    public Mail(String recepient, String message) throws MessagingException {
        System.out.println("WAITTTTT");
        Properties p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", "587");

        String myEmail = "hariharanmvasanth@gmail.com";
        String password = "lllt mxwz jkvz zhbu";

        Session s = Session.getDefaultInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, password);
            }
        });
        Message m = prepareMessage(s, myEmail,recepient,message);
        Transport.send(m);
    }

    private static Message prepareMessage(Session s, String myEmail, String recepient,String message) throws MessagingException {
        Message m = new MimeMessage(s);
        m.setFrom(new InternetAddress(myEmail));
        m.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
        m.setSubject("Train Bokking Status");
        m.setText(message);
        return m;
    }
}
