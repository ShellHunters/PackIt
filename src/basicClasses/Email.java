package basicClasses;

import interfaceMagazinier.providers.SendEmailMessageController;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Email {
    public static boolean ItSent =false;
    private static final String senderMail="";
    private static final String senderPassword="";
    static public void send() {
        System.out.println("Prepare sending msg ...");
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderMail, senderPassword);
            }
        });

        Message msg = prepareMessage(session);

        try {
            Transport.send(msg);
            ItSent= true;
            System.out.println("Message send successfully !");

        } catch (MessagingException me) {
            me.printStackTrace();
            ItSent= true;
        }
    }
    private static Message prepareMessage(Session session){
        Message msg = null;
        try {
            msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderMail));

            InternetAddress recipientAddress = new InternetAddress( SendEmailMessageController.msgRecipients);
                    //new InternetAddress[SendEmailMessageController.msgRecipients.size()];
         //   for (int i = 0; i < SendEmailMessageController.msgRecipients.size(); i++)
           //     recipientAddress[i] = new InternetAddress(SendEmailMessageController.msgRecipients.get(i));

            msg.setRecipient(Message.RecipientType.TO,recipientAddress);

            msg.setSubject(SendEmailMessageController.msgSubject);
            msg.setText(SendEmailMessageController.msgContent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return msg;
    }


}
