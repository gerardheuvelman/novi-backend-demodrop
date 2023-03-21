package nl.ultimateapps.demoDrop.Services;

import lombok.AllArgsConstructor;
import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.EmailDetails;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Repositories.UserRepository;
import nl.ultimateapps.demoDrop.Utils.HyperlinkBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;

    private final UserRepository userRepository;

    private String sender;

    public EmailServiceImpl(JavaMailSender javaMailSender, UserRepository userRepository, @Value("${spring.mail.username}") String sender) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.sender = sender;
    }

    public String sendSimpleMail(EmailDetails emailDetails) { // AUTH ONLY
        if (userRepository.findById(emailDetails.getRecipientUsername()).isEmpty()) {
            throw new UsernameNotFoundException(emailDetails.getRecipientUsername());
        }
        User recipient = userRepository.findById(emailDetails.getRecipientUsername()).get();
        String recipientEmail = recipient.getEmail();

        // Try block to check for exceptions
        try {
            // Creating a simple mail message
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            // Setting up necessary details
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(recipientEmail);
            simpleMailMessage.setText(emailDetails.getMsgBody());
            simpleMailMessage.setSubject(emailDetails.getSubject());

            // Sending the mail
            javaMailSender.send(simpleMailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    public String sendMailWithAttachment(EmailDetails emailDetails) { // AUTH ONLY
        if (userRepository.findById(emailDetails.getRecipientUsername()).isEmpty()) {
            throw new UsernameNotFoundException(emailDetails.getRecipientUsername());
        }
        User recipient = userRepository.findById(emailDetails.getRecipientUsername()).get();
        String recipientEmail = recipient.getEmail();
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            // Setting multipart as true for attachments to be sent
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(recipientEmail);
            mimeMessageHelper.setText(emailDetails.getMsgBody());
            mimeMessageHelper.setSubject(
                    emailDetails.getSubject());

            // Adding the attachment
            FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
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
        EmailDetails emailDetails = new EmailDetails(emailRecipient.getUsername(), emailSubject, emailBody, null);
        return sendSimpleMail(emailDetails);
    }

}
