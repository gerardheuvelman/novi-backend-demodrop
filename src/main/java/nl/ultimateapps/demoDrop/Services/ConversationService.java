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
import nl.ultimateapps.demoDrop.Utils.AuthHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Instant;
import java.util.*;

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

    @Getter
    @Setter
    private EmailService emailService;

    public ArrayList<ConversationDto> getConversations(int limit) { // ADMIN ONLY
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        ArrayList<ConversationDto> conversationDtoArrayList = new ArrayList<>();
        Iterable<Conversation> conversationIterable = conversationRepository.findAllByOrderByLatestReplyDateDesc();
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


    public List<ConversationDto> getPersonalConversations(String username) { // AUTH ONLY
        if (!AuthHelper.checkAuthorization(username)) { // check associative authorization
            throw new AccessDeniedException("User " + AuthHelper.getPrincipalUsername() + " has insufficient rights to retrieve conversation associated with user " + username);
        }
        // First, get the User object
        User user = userRepository.findById(username).get();
        // get the conversations in which te user has the role "producer"
        Iterable<Conversation> conversationsAsProducer = conversationRepository.findByProducerOrderByLatestReplyDateDesc(user);
        // now, get the conversations for this user by the field "interestedUser"
        Iterable<Conversation> conversationsAsInterestedUser = conversationRepository.findByInterestedUserOrderByLatestReplyDateDesc(user);
        // splice these two lists together into a new list;
        List<Conversation> completeConversationList = new ArrayList<>();
        conversationsAsProducer.forEach(completeConversationList::add);
        conversationsAsInterestedUser.forEach(completeConversationList::add);
        // Since correct sorting was lost during the splice, re-apply it:
        Comparator<Conversation> conversationComparator = new Comparator<Conversation>() {
            @Override
            public int compare(Conversation o1, Conversation o2) {
                return (o2.getLatestReplyDate().compareTo(o1.getLatestReplyDate()));
            }
        };
        completeConversationList.sort(conversationComparator);
        // Translate ccmpleteConversationList to a final resultlist of corresponding DTO's
        List<ConversationDto> resultList = new ArrayList<>();
        for (Conversation conversation : completeConversationList) {
            ConversationDto conversationDto = ConversationMapper.mapToDto(conversation);
            resultList.add(conversationDto);
        }
        return resultList;
    }

    public ConversationDto getConversation(long conversationId) throws AccessDeniedException { // AUTH ONLY
        if (!conversationRepository.findById(conversationId).isPresent()) {
            throw new RecordNotFoundException();
        }
        Conversation conversation = conversationRepository.findById(conversationId).get();
        if (!AuthHelper.checkAuthorization(conversation)) { // check associative authorization
            throw new AccessDeniedException("User" + AuthHelper.getPrincipalUsername() + " has insufficient rights to retrieve conversation " + conversationId);
        }
        return ConversationMapper.mapToDto(conversation);
    }

    public ConversationDto createConversation(ConversationDto conversationDto) throws UserPrincipalNotFoundException, AccessDeniedException { // AUTH ONLY
        long demoId = conversationDto.getDemo().getDemoId();
        // get the demo object form the repository
        if (!demoRepository.findById(demoId).isPresent()) {
            throw new RecordNotFoundException();
        }
        Demo retrievedDemo = demoRepository.findById(demoId).get();

        // Check associative authorization
        if (!AuthHelper.checkAuthorization(retrievedDemo)) {
            throw new AccessDeniedException("User " + AuthHelper.getPrincipalUsername() + " has insufficient rights to create a new conversation for demo " + demoId);
        }
        Conversation conversation = ConversationMapper.mapToModel(conversationDto);
        Date now = Date.from(Instant.now());
        conversation.setCreatedDate(now);
        conversation.setLatestReplyDate(now);
        conversation.setDemo(retrievedDemo);
        // set the producer through the demo relationship:
        conversation.setProducer(retrievedDemo.getUser());
        conversation.setReadByInterestedUser(true);
        conversation.setReadByProducer(false);
        // set the field "interestedUser" from the current security principal.
        String currentPrincipalName = AuthHelper.getPrincipalUsername();
        if (!userRepository.findById(currentPrincipalName).isPresent()) {
            throw new UserPrincipalNotFoundException(currentPrincipalName);
        }
        User currentPrincipal = userRepository.findById(currentPrincipalName).get();
        conversation.setInterestedUser(currentPrincipal);
        Conversation savedConversation = conversationRepository.save(conversation);
        // before returning, send a "New message" email to the producer of the demo and log it :
        String sendResult = emailService.SendNewMessageEmail(savedConversation, true);
        System.out.println(sendResult);
        return ConversationMapper.mapToDto(savedConversation);
    }

    public ConversationDto updateConversation(long id, ConversationDto conversationDto) throws RecordNotFoundException, AccessDeniedException { // AUTH ONLY
        if (!conversationRepository.findById(id).isPresent()) {
            throw new RecordNotFoundException();
        }
        Conversation conversation = conversationRepository.findById(id).get();

        if (!AuthHelper.checkAuthorization(conversation)) { // check associative authorization
            throw new AccessDeniedException("User" + AuthHelper.getPrincipalUsername() + " has insufficient rights to edit conversation " + conversation.getConversationId());
        }
        conversation.setSubject((conversationDto.getSubject()));
        conversation.setBody((conversationDto.getBody()));
        conversation.setLatestReplyDate(Date.from(Instant.now()));
        // set the 'readBy flags'
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (currentPrincipalName.equals(conversation.getInterestedUser().getUsername())) {
            conversation.setReadByInterestedUser(true);
            conversation.setReadByProducer(false);
        } else {
            conversation.setReadByInterestedUser(false);
            conversation.setReadByProducer(true);
        }
        Conversation savedConversation = conversationRepository.save(conversation);
        // before returning, send a "New message" email to the producer of the demo and log it :
        String sendResult = emailService.SendNewMessageEmail(savedConversation, false);
        System.out.println(sendResult);
        return ConversationMapper.mapToDto(savedConversation);

    }

    public ConversationDto markConversationAsRead(long id) throws AccessDeniedException { // AUTH ONLY
        if (conversationRepository.findById(id).isPresent()) {
            Conversation conversation = conversationRepository.findById(id).get();
            if (!AuthHelper.checkAuthorization(conversation)) { // check associative authorization
                throw new AccessDeniedException("User " + AuthHelper.getPrincipalUsername() + " has insufficient rights to mark conversation" + conversation.getConversationId() + " as read.");
            }
            String currentUserName = AuthHelper.getPrincipalUsername();
            if (currentUserName.equals(conversation.getInterestedUser().getUsername())) {
                conversation.setReadByInterestedUser(true);
            } else conversation.setReadByProducer(true);
            conversationRepository.save(conversation);
            return ConversationMapper.mapToDto(conversation);
        } else throw new RecordNotFoundException();
    }

    public long deleteConversations() { //ADMIN ONLY
        if ((conversationRepository.findAll().size()) <= 0) {
            throw new RecordNotFoundException();
        }
        List<Conversation> conversations = conversationRepository.findAll();
        long numDeletedConversations = 0;
        for (Conversation conversation : conversations) {
            conversationRepository.delete(conversation);
            numDeletedConversations++;
        }
        return numDeletedConversations;
    }

    public long deleteConversation(long conversationId) { // ADMIN ONLY
        if (conversationRepository.findById(conversationId).isPresent()) {
            Conversation conversation = conversationRepository.findById(conversationId).get();
            // Check associative authorization
            if (!AuthHelper.checkAuthorization(conversation)) {
                throw new AccessDeniedException("User has insufficient rights to delete conversation " + conversationId);
            }
            conversationRepository.deleteById(conversationId);
            return conversationId;
        } else {
            throw new RecordNotFoundException();
        }
    }
}