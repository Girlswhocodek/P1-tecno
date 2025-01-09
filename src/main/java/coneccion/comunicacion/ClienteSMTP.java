/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coneccion.comunicacion;

import coneccion.utils.Email;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author dell
 */
public class ClienteSMTP implements Runnable {

    /*private final static String PORT_SMTP = "25";
    private final static String PROTOCOL = "smtp";
    private final static String HOST = "mail.tecnoweb.org.bo";
    private final static String USER = "grupo18sa";
    private final static String PASSWORD = "grup018grup018*";
    private final static String MAIL = "grupo18sa@tecnoweb.org.bo";
    private final static String MAIL_PASSWORD = "grup018grup018*";
     */
    private Email email;

    //   mejor si no usas por gmail y directo usa el de tu grupo
    private final static String PORT_SMTP = "465";
    private final static String PROTOCOL = "smtp";
    private final static String HOST = "smtp.gmail.com";
    private final static String USER = "karolile.ortiz@gmail.com";
    private final static String MAIL = "karolile.ortiz@gmail.com";
    private final static String MAIL_PASSWORD = "pikuuuxcsmtefgyw";

    public ClienteSMTP(Email email) {
        this.email = email;
    }

    @Override
    public void run() {
        // Mail mailSender = new Mail();
        // mailSender.send(email);

        Properties properties = new Properties();
        // properties.put("mail.transport.protocol", PROTOCOL);//cuando uses tecnoweb
        properties.setProperty("mail.smtp.host", HOST);
        properties.setProperty("mail.smtp.port", PORT_SMTP);
        // properties.setProperty("mail.smtp.tls.enable", "true");//cuando uses tecnoweb
        properties.setProperty("mail.smtp.ssl.enable", "true");//cuando usen Gmail
        //   properties.setProperty("mail.smtp.auth", "false");//cuando uses tecnoweb   
        properties.setProperty("mail.smtp.auth", "true");//cuando uses gmail 

        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println(USER + " - " + MAIL);
                return new PasswordAuthentication(USER, MAIL_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAIL));

            InternetAddress[] toAddresses = {new InternetAddress(email.getTo())};

            message.setRecipients(MimeMessage.RecipientType.TO, toAddresses);
            message.setSubject(email.getSubject());

            Multipart multipart = new MimeMultipart("alternative");
            MimeBodyPart htmlPart = new MimeBodyPart();

            htmlPart.setContent(email.getMessage(), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);
            message.saveChanges();

            Transport.send(message);
            System.out.println("MENSAJE ENVIADO CON EXITO");
        } catch (NoSuchProviderException | AddressException ex) {
            Logger.getLogger(ClienteSMTP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ClienteSMTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
