package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.ConversationMapper;
import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Repositories.ConversationRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import lombok.*;

@Service
@AllArgsConstructor
public class ConversationService {

    @Getter
    @Setter
    private ConversationRepository repos;

    public ArrayList<ConversationDto> getConversations() {
        Iterable<Conversation> allConversations = repos.findAll();
        ArrayList<ConversationDto> resultList = new ArrayList<>();
        for (Conversation conversation : allConversations) {
            ConversationDto newConversationDto = ConversationMapper.mapToDto(conversation);
            resultList.add(newConversationDto);
        }
        return resultList;
    }

    public ConversationDto getConversation(long id) {
        if (repos.findById(id).isPresent()) {
            Conversation conversation = repos.findById(id).get();
            ConversationDto conversationDto = ConversationMapper.mapToDto(conversation);
            return conversationDto;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long createConversation(ConversationDto conversationDto) {
        Conversation conversation =  ConversationMapper.mapToModel(conversationDto);
        Conversation savedConversation = repos.save(conversation);
        return savedConversation.getId();
    }

    public long updateConversation(long id, ConversationDto conversationDto) {
        if (repos.findById(id).isPresent()) {
            Conversation conversation = repos.findById(id).get();
            conversation.setSubject((conversationDto.getSubject()));
            conversation.setCreatedDate((conversationDto.getCreatedDate()));
            conversation.setLatestReplyDate((conversationDto.getLatestReplyDate()));
            repos.save(conversation);
            return conversation.getId();

        } else {
            throw new RecordNotFoundException();
        }
    }

    public long partialUpdateConversation(long id, ConversationDto ConversationDto) {
        if (repos.findById(id).isPresent()) {
            Conversation Conversation = repos.findById(id).get();
            if (Conversation.getBody() != null) {
                Conversation.setBody(ConversationDto.getBody());
            }
            repos.save(Conversation);
            return Conversation.getId();
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long deleteConversation(long id) {
        if (repos.findById(id).isPresent()) {
            Conversation conversation = repos.findById(id).get();
            long retrievedId = conversation.getId();
            repos.deleteById(id);
            return retrievedId;
        } else {
            throw new RecordNotFoundException();
        }
    }
}