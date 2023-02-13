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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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

    public ArrayList<ConversationDto> getConversations(int limit) {
        ArrayList<ConversationDto> conversationDtoArrayList = new ArrayList<>();
        Iterable<Conversation> conversationIterable = conversationRepository.findAllByOrderByCreatedDateDesc();
        ArrayList<Conversation> conversationArrayList = new ArrayList<>();
        conversationIterable.forEach(conversationArrayList::add);
        int numResults = conversationArrayList.size();
        if (limit == 0) {
            // return full list
            for (Conversation conversation : conversationArrayList) {
                ConversationDto conversationDto = ConversationMapper.mapToDto(conversation);
                conversationDtoArrayList.add(conversationDto);
            }
        } else {
            // return limited list
            for (int i = 0; i < (Math.min(numResults, limit)); i++) {
                ConversationDto conversationDto = ConversationMapper.mapToDto(conversationArrayList.get(i));
                conversationDtoArrayList.add(conversationDto);
            }
        }
        return conversationDtoArrayList;
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

    public ConversationDto startConversation(ConversationDto conversationDto) throws UserPrincipalNotFoundException {
        Demo demo = conversationDto.getDemo();
        demoRepository.save(demo); // dit is nodig omdat het demo object anders "transient" is.
        Conversation conversation =  ConversationMapper.mapToModel(conversationDto);
        conversation.setCreatedDate(Date.from(Instant.now()));
        conversation.setLatestReplyDate(Date.from(Instant.now()));
        conversation.setDemo(demo);
        // set the producer through the demo relationship:
        conversation.setProducer(demo.getUser());
        conversation.setReadByInterestedUser(true);
        conversation.setReadByProducer(false);

        // Set the current User object (interestedUser) from the Security context (NOT the request body!)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (userRepository.findById(currentPrincipalName) != null) {
            conversation.setInterestedUser(userRepository.findById(currentPrincipalName).get());
        } else throw new UserPrincipalNotFoundException(currentPrincipalName);

        Conversation savedConversation = conversationRepository.save(conversation);
        return ConversationMapper.mapToDto(savedConversation);
    }

    public ConversationDto replyToConversation(long id, ConversationDto conversationDto) throws RecordNotFoundException {
        if (conversationRepository.findById(id).isPresent()) {
            Conversation conversation = conversationRepository.findById(id).get();
            conversation.setSubject((conversationDto.getSubject()));
            conversation.setCreatedDate((conversationDto.getCreatedDate()));
            conversation.setLatestReplyDate(Date.from(Instant.now()));
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            if (currentPrincipalName.equals(conversation.getInterestedUser().getUsername()) ) {
                conversation.setReadByInterestedUser(true);
                conversation.setReadByProducer(false);
            } else {
                conversation.setReadByInterestedUser(false);
                conversation.setReadByProducer(true);
            }
            return ConversationMapper.mapToDto( conversationRepository.save(conversation));
        } else throw new RecordNotFoundException();
    }

    public ConversationDto markConversationAsRead(long id) {
        if (conversationRepository.findById(id).isPresent()) {
            Conversation conversation = conversationRepository.findById(id).get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            if (currentPrincipalName.equals(conversation.getInterestedUser().getUsername())) {
                conversation.setReadByInterestedUser(true);
            } else conversation.setReadByProducer(true);
            conversationRepository.save(conversation);
            return ConversationMapper.mapToDto(conversation);
        } else throw new RecordNotFoundException();
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