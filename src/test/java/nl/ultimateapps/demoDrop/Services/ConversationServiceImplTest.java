package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.DemoDropApplication;
import nl.ultimateapps.demoDrop.Dtos.output.ConversationDto;
import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Models.*;
import nl.ultimateapps.demoDrop.Repositories.ConversationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {DemoDropApplication.class})
public class ConversationServiceImplTest {

    Date date;
    Genre genre;

    Authority authority;

    Set<Authority> authorityList = new HashSet<>();

    User user;
    AudioFile audioFile;

    DemoDto demoDto1;

    DemoDto demoDto2;

    Collection grantedAuthorities;

    SimpleGrantedAuthority demoDropGrantedAuthority;

    @Autowired
    private ConversationService conversationService;

    @MockBean
    private ConversationRepository conversationRepository;

    Conversation conversation;

    @BeforeEach
    void setUp() {

        date = new Date();
        genre = new Genre("Alternative Dance", null);

        authority = new Authority("gerard", "ROLE_ADMIN");
        authorityList.add(authority);
        demoDropGrantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        grantedAuthorities = List.of(demoDropGrantedAuthority);

        user = new User("gerard", null, true, "fakeKey2", "gerardheuvelman@gmail.com", date, authorityList, null, null, null, null);
        audioFile = new AudioFile(3001, date, "PrimeAudio.mp3", null);
        demoDto1 = new DemoDto(1001L, date, "Prime Audio", 123D, 123D, audioFile, genre, user, null, null);
        demoDto2 = new DemoDto(1002L, date, "AudioSecundo", 123D, 123D, audioFile, genre, user, null, null);
    }

    @Test
    public void testGetConversation() {

        //GIVEN
        conversation = new Conversation(2001L, null, null, "hello , world!", null, true, true, null, user, user);

        Authentication authentication = Mockito.mock(Authentication.class);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);



        Mockito.when(conversationRepository.findById(conversation.getConversationId())).thenReturn(Optional.of(conversation));

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        Mockito.when(authentication.isAuthenticated()).thenReturn(true);

        Mockito.when(authentication.getAuthorities()).thenReturn(grantedAuthorities);



        Long Id = 2001L;
        String expected = "hello , world!";

        //WHEN
        ConversationDto found = conversationService.getConversation(Id);

        //THEN
        assertEquals(expected, found.getSubject());
    }
}