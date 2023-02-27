package nl.ultimateapps.demoDrop.Services;

import io.github.cdimascio.dotenv.Dotenv;
import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.EmailDetails;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Utils.HyperlinkBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;

    private String sender;

    public EmailServiceImpl(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String sender) {
        System.out.println(sender);
        this.javaMailSender = javaMailSender;
        this.sender = sender;
    }

    public String sendSimpleMail(EmailDetails details) {
        // Try block to check for exceptions
        try {
            // Creating a simple mail message
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            // Setting up necessary details
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(details.getRecipient());
            simpleMailMessage.setText(details.getMsgBody());
            simpleMailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(simpleMailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    public String sendMailWithAttachment(EmailDetails details) {
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            // Setting multipart as true for attachments to be sent
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                    details.getSubject());

            // Adding the attachment
            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            mimeMessageHelper.addAttachment(file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        // Catch block to handle MessagingException
        catch (MessagingException e) {

            // Display message when exception occurred
            return "Error while sending mail!!!";
        }
    }

    @Override
    public String SendNewMessageEmail(Conversation conversation, boolean newConversation) {
        User msgSender, emailRecipient;
        // get the message sender
        if (conversation.isReadByInterestedUser() == true) {
            msgSender = conversation.getInterestedUser();
            emailRecipient = conversation.getProducer();
        } else {
            msgSender = conversation.getProducer();
            emailRecipient = conversation.getInterestedUser();
        }

        String emailSubject;
        if (newConversation) {
            emailSubject = "DemoDrop  - A user has shown interest in your demo.";
        } else emailSubject = "DemoDrop  - You have received a reply.";

        // compose the message body.
        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append(msgSender);
        if (newConversation) {
            bodyBuilder.append(" has shown interest in your demo \"");
        } else {
            bodyBuilder.append(" has replied to your message regarding \"");
        }
        bodyBuilder.append(conversation.getDemo().getTitle());
        bodyBuilder.append("\".\nClick on the link below to read the message.\n\n");
        HyperlinkBuilder hyperlinkBuilder = new HyperlinkBuilder();
        String hyperLink= hyperlinkBuilder.buildConversationHyperlink(conversation);
        bodyBuilder.append(hyperLink);
        String emailBody = bodyBuilder.toString();

        // Send the email:
        EmailDetails emailDetails = new EmailDetails(emailRecipient.getEmail(), emailSubject, emailBody, null);
        return sendSimpleMail(emailDetails);
    }

}
