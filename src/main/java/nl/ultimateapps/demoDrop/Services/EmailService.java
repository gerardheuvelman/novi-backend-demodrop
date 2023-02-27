package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.EmailDetails;

public interface EmailService {

    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);

    String SendNewMessageEmail(Conversation conversation, boolean newConversation);
}

