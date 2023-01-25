package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.mappers.ConversationMapper;
import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Repositories.ConversationRepository;
import nl.ultimateapps.demoDrop.Repositories.DemoRepository;
import nl.ultimateapps.demoDrop.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Service
@AllArgsConstructor
public class ConversationService {

    @Getter
    @Setter
    private ConversationRepository conversationRepository;

    @Getter
    @Setter
    private DemoRepository demoRepository;

    @Getter
    @Setter
    private UserRepository userRepository;

    public ArrayList<ConversationDto> getConversations() {
        Iterable<Conversation> allConversations = conversationRepository.findAll();
        ArrayList<ConversationDto> resultList = new ArrayList<>();
        for (Conversation conversation : allConversations) {
            ConversationDto newConversationDto = ConversationMapper.mapToDto(conversation);
            resultList.add(newConversationDto);
        }
        return resultList;
    }

    public List<ConversationDto> getPersonalConversations(String username) {
        // First, get the User object
        User user = userRepository.findById(username).get();
        // Second, fetch personal demo list
        Iterable<Demo> demos = demoRepository.findByUserOrderByCreatedDateDesc(user);
        List<Conversation> conversationsFromDemos = new ArrayList<>();
        // next, get the conversations associated with these demos, and add them to this list.
        for (Demo demo : demos) {
            for (Conversation conversation : conversationRepository.findByDemoOrderByLatestReplyDateDesc(demo)) {
                conversationsFromDemos.add(conversation);
            }
        }
        // now, get the conversations for this user by the field "interestedUser"
        List<Conversation> conversationsFromInterestedUserField = new ArrayList<>();
        for (Conversation conversation: conversationRepository.findByInterestedUserOrderByLatestReplyDateDesc(user)) {
            conversationsFromInterestedUserField.add(conversation);
        }
        // Finally, splice these two lists together, and re-apply sorting
        List<Conversation> completeConversationList = new ArrayList<>();
        completeConversationList.addAll(conversationsFromDemos);
        completeConversationList.addAll(conversationsFromInterestedUserField);
//        completeConversationList.sort(); TODO implementeren
        List<ConversationDto> resultList = new ArrayList<>();
        for (Conversation conversation: completeConversationList) {
            ConversationDto conversationDto = ConversationMapper.mapToDto(conversation);
            resultList.add(conversationDto);
        }
        return resultList;
    }

    public ConversationDto getConversation(long id) {
        if (conversationRepository.findById(id).isPresent()) {
            Conversation conversation = conversationRepository.findById(id).get();
            ConversationDto conversationDto = ConversationMapper.mapToDto(conversation);
            return conversationDto;
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long createConversation(ConversationDto conversationDto) {
        Conversation conversation =  ConversationMapper.mapToModel(conversationDto);
        Conversation savedConversation = conversationRepository.save(conversation);
        return savedConversation.getConversationId();
    }

    public long updateConversation(long id, ConversationDto conversationDto) {
        if (conversationRepository.findById(id).isPresent()) {
            Conversation conversation = conversationRepository.findById(id).get();
            conversation.setSubject((conversationDto.getSubject()));
            conversation.setCreatedDate((conversationDto.getCreatedDate()));
            conversation.setLatestReplyDate((conversationDto.getLatestReplyDate()));
            conversationRepository.save(conversation);
            return conversation.getConversationId();

        } else {
            throw new RecordNotFoundException();
        }
    }

    public long partialUpdateConversation(long id, ConversationDto ConversationDto) {
        if (conversationRepository.findById(id).isPresent()) {
            Conversation Conversation = conversationRepository.findById(id).get();
            if (Conversation.getBody() != null) {
                Conversation.setBody(ConversationDto.getBody());
            }
            conversationRepository.save(Conversation);
            return Conversation.getConversationId();
        } else {
            throw new RecordNotFoundException();
        }
    }

    public long deleteConversations() {
        if (conversationRepository.findAll() != null) {
            List<Conversation> conversations = conversationRepository.findAll();
            long numDeletedConversations = 0;
            for ( Conversation conversation : conversations) {
                conversationRepository.delete(conversation);
                numDeletedConversations++;
            }
            return numDeletedConversations;
        } else {
            throw new RecordNotFoundException();
        }
    }


    public long deleteConversation(long id) {
        if (conversationRepository.findById(id).isPresent()) {
            Conversation conversation = conversationRepository.findById(id).get();
            long retrievedId = conversation.getConversationId();
            conversationRepository.deleteById(id);
            return retrievedId;
        } else {
            throw new RecordNotFoundException();
        }
    }
}