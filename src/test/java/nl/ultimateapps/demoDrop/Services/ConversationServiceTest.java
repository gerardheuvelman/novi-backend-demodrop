package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.ConversationMapper;
import nl.ultimateapps.demoDrop.Models.*;
import nl.ultimateapps.demoDrop.Repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {ConversationServiceImpl.class})
public class ConversationServiceTest extends ServiceTest {

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private DemoRepository demoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ConversationServiceImpl conversationServiceImpl;

    @BeforeEach
    public void setUp() {
        // Re-initialize the entire in-memory database using the base class setup method.
        super.reInitializeInMemoryDatabase();
    }

    @Test
    public void GetConversationsReturnAListOfConversationDtos() {
        //ARRANGE
        List<ConversationDto> expectedConversationDtoList = allConversationDtos;

        //GIVEN
        Mockito.when(conversationRepository.findAllByOrderByLatestReplyDateDesc()).thenReturn(allConversations);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        //ACT
        //WHEN
        List<ConversationDto> actualConversationDtoList = conversationServiceImpl.getConversations(0);

        //ASSERT
        //THEN
        assertEquals(expectedConversationDtoList, actualConversationDtoList);
    }

    @Test
    public void GetConversationReturnsAConversationDto() {
        //ARRANGE
        Conversation conversation = aboutPrimeAudio;
        conversation.setProducer(user);
        conversation.setInterestedUser(admin);
        long conversationId = conversation.getConversationId();

        ConversationDto expectedConversationDto = aboutPrimeAudioDto;

        //GIVEN
        Mockito.when(conversationRepository.findById(conversation.getConversationId())).thenReturn(Optional.of(conversation));
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getAuthorities()).thenReturn(grantedAuthorities);

        //ACT
        //WHEN
        ConversationDto actualConversationDto = conversationServiceImpl.getConversation(conversationId);


        //ASSERT
        //THEN
        assertEquals(expectedConversationDto.getSubject(), actualConversationDto.getSubject());
    }

    @Test
    public void CreateConversationReturnsAConversationDto() throws UserPrincipalNotFoundException {
        //ARRANGE
        Demo demo = primeAudio;
        demo.setUser(user);
        ConversationDto newConversationDto = new ConversationDto(null, earlierDate, earlierDate, "About Prime Audio Again", "body3", false, true, null, null, null);
        newConversationDto.setDemo(demo);

        Conversation newConversation = ConversationMapper.mapToModel(newConversationDto);
        newConversation.setConversationId(2003L);

        ConversationDto expectedConversationDto = ConversationMapper.mapToDto(newConversation);

        //GIVEN
        Mockito.when(userRepository.findById(authentication.getName())).thenReturn(Optional.of(user));
        Mockito.when(demoRepository.findById(demo.getDemoId())).thenReturn(Optional.of(demo));
        Mockito.when(conversationRepository.save(any(Conversation.class))).thenReturn(newConversation);
        // Fake a user with admin authority
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getAuthorities()).thenReturn(grantedAuthorities);

        String expectedSubject = newConversationDto.getSubject();

        //ACT
        //WHEN
        ConversationDto returnedConversationDto = conversationServiceImpl.createConversation(newConversationDto);

        //ASSERT
        //THEN
        assertEquals(expectedSubject, returnedConversationDto.getSubject());
    }

    @Test
    public void updateConversationReturnsAConversationDto() {
        //ARRANGE

        Conversation conversation = aboutPrimeAudio;
        conversation.setProducer(user);
        conversation.setInterestedUser(admin);

        ConversationDto inputConversationDto = aboutPrimeAudioDto;

        ConversationDto expectedConversationDto = ConversationMapper.mapToDto(conversation);

        //GIVEN
        Mockito.when(conversationRepository.findById(conversation.getConversationId())).thenReturn(Optional.of(conversation));
        Mockito.when(conversationRepository.save(any(Conversation.class))).thenReturn(conversation);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn(user.getUsername());

        //ACT
        ConversationDto actualConversationDto = conversationServiceImpl.updateConversation(conversation.getConversationId(), inputConversationDto);
        //ASSERT
        assertEquals(expectedConversationDto.getSubject(), actualConversationDto.getSubject());
    }

    @Test
    public void markConversationAsReadReturnsACorrectConversationDto() {
        //ARRANGE
        User producer = user;
        User interestedUser = admin;
        Conversation conversation = aboutPrimeAudio;
        Conversation savedConversation = conversation;
        savedConversation.setReadByProducer(false);
        savedConversation.setProducer(producer);
        conversation.setInterestedUser(interestedUser);

        //GIVEN
        Mockito.when(conversationRepository.findById(conversation.getConversationId())).thenReturn(Optional.of(conversation));
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn(user.getUsername());
        Mockito.when(conversationRepository.save(any(Conversation.class))).thenReturn(savedConversation);

        // ACT
        ConversationDto actualConversationDto = conversationServiceImpl.markConversationAsRead(conversation.getConversationId());

        //ASSERT
        assertEquals(false, actualConversationDto.isReadBbyProducer());
        assertEquals(false, actualConversationDto.isReadByInterestedUser());

    }

    @Test
    public void deleteConversationsReturnsTheNumberOfDeletedConversations() {
        //ARRANGE
        long expectedNumDeletedConversations = 2L;

        //GIVEN
        Mockito.when((conversationRepository.findAll())).thenReturn(allConversations);

        //ACT
        long actuaNumDeletedConversations = conversationServiceImpl.deleteConversations();


        //ASSERT
        assertEquals(expectedNumDeletedConversations, actuaNumDeletedConversations);
    }

    @Test
    public void deleteConversationReturnsTheIdOfTheDeletedConversation() {
        //ARRANGE
        Conversation conversation = aboutPrimeAudio;
        conversation.setProducer(user);
        conversation.setInterestedUser(admin);

        long expectedConversationId = conversation.getConversationId();

        //GIVEN
        Mockito.when(conversationRepository.findById(conversation.getConversationId())).thenReturn(Optional.of(conversation));
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getAuthorities()).thenReturn(grantedAuthorities);

        //ACT
        long actualConversationId = conversationServiceImpl.deleteConversation(conversation.getConversationId());


        //ASSERT
        assertEquals(expectedConversationId, actualConversationId);
    }
}