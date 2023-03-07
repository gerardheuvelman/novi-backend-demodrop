package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;

public interface ConversationService {
    ArrayList<ConversationDto> getConversations(int limit);

    List<ConversationDto> getPersonalConversations(String username);

    ConversationDto getConversation(long conversationId) throws AccessDeniedException;

    ConversationDto createConversation(ConversationDto conversationDto) throws UserPrincipalNotFoundException, AccessDeniedException;

    ConversationDto updateConversation(long id, ConversationDto conversationDto) throws RecordNotFoundException, AccessDeniedException;

    ConversationDto markConversationAsRead(long id) throws AccessDeniedException;

    long deleteConversations();

    long deleteConversation(long conversationId);
}
