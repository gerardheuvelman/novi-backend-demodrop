package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.*;
import nl.ultimateapps.demoDrop.Helpers.mappers.*;
import nl.ultimateapps.demoDrop.Models.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTest implements IServiceTest {

    // The code in this class declares and initializes various objects and collections, to allow for easy testing of any service in the DemoDrop Spring Boot application.
    // To write quick and effective tests, you must understand the data presented. PLEASE READ THE FOLLOWING PARAGRAPH!!

    // The code describes two users: "user" and "admin"; User had a role of "user" and admin has a role of "admin".
    // User has a dance demo named "Prime Audio" and admin has a rock demo named "Audio Secundo".
    // Both users have favorited each other's demos and inquired about them through demoDrop's messaging system.

    // THIS IS AN ABSTRACT CLASS and therefore must be inherited from.
    // In the following section we present namy class members that are available to all subclasses:

    // TWO COPIES OF EACH ENTITY

    protected User user;
    protected User admin;

    protected AudioFile primeAudioFile;
    protected AudioFile audioSecundoFile;
    protected Authority userAuthority;
    protected Authority adminAuthority;
    protected Conversation aboutPrimeAudio;
    protected Conversation aboutAudioSecundo;
    protected Demo primeAudio;
    protected Demo audioSecundo;
    protected EmailDetails demoCreatedEmail;
    protected EmailDetails sentMessageEmail;
    protected Genre dance;
    protected Genre rock;

    // TWO COPIES OF EACH DTO:
    protected AudioFileDto primeAudioFileDto;
    protected AudioFileDto audioSecundoFileDto;
    protected AuthorityDto userAuthorityDto;
    protected AuthorityDto adminAuthorityDto;
    protected ConversationDto aboutPrimeAudioDto;
    protected ConversationDto aboutAudioSecundoDto;
    protected DemoDto primeAudioDto;
    protected DemoDto audioSecundoDto;
    protected EmailDetailsDto demoCreatedEmailDto;
    protected EmailDetailsDto sentMessageEmailDto;
    protected GenreDto danceDto;
    protected GenreDto rockDto;
    protected UserDto userUserDto;
    protected UserDto adminUserDto;

    protected UserPublicDto userUserPublicDto;
    protected UserPublicDto adminUserPublicDto;


    // COLLECTIONS OF ENTITIES
    protected Set<Authority> userAuthoritySet;
    protected Set<Authority> adminAuthoritySet;

    // COLLECTIONS OF DTOS
    protected Set<AuthorityDto> authorityDtoSetOfUser;
    protected Set<AuthorityDto> authorityDtoSetOfAdmin;

    // FILE STORAGE OBJECTS
    @Value("${my.upload_location}")
    protected String fileStorageLocation;
    protected Path uploadPath, primeAudioFilePath, audioSecundoFilePath;

    protected String primeAudioFileName, audioSecundoFilename;

    // OTHER REQUIRED OBJECTS
    protected MultipartFile primeAudioMultipartFile, audioSecundoMultipartFile;
    protected Resource primeAudioResource, audioSecundoResource;
    protected SecurityContext securityContext;
    protected Authentication authentication;
    protected SimpleGrantedAuthority userSimpleGrantedAuthority, adminSimpleGrantedAuthority;
    protected Date earlierDate, laterDate;

    // LISTS
    protected List<AudioFile> allAudioFiles;
    protected List<AudioFileDto> allAudioFileDtos;
    protected List<Authority> allAuthorities;
    protected List<AuthorityDto> allAuthorityDtos;
    protected List<Conversation> allConversations;
    protected List<ConversationDto> allConversationDtos;
    protected List<Demo> allDemos;
    protected List<DemoDto> allDemoDtos;
    protected List<EmailDetails> allEmailDetailss;
    protected List<EmailDetailsDto> allEmailDetailsDtos;
    protected List<Genre> allGenres;
    protected List<GenreDto> allGenreDtos;
    protected List<User> allUsers;
    protected List<UserDto> allUserDtos;
    protected List<UserPublicDto> allUserPublicDtos;


    // LISTS FROM DEMO
    protected List<Demo> usersProducedDemoList;
    protected List<DemoDto> usersProducedDemoDtoList;
    protected List<Demo> adminsProducedDemoList;
    protected List<DemoDto> adminsProducedDemoDtoList;
    protected List<Demo> usersFavoriteDemoList;
    protected List<DemoDto> usersFavoriteDemoDtoList;
    protected List<Demo> adminsFavoriteDemoList;
    protected List<DemoDto> adminsFavoriteDemoDtoList;
    protected List<Conversation> usersConversationsAsProducerList;
    protected List<ConversationDto> usersConversationsAsProducerDtoList;
    protected List<Conversation> adminsConversationsAsProducerList;
    protected List<ConversationDto> adminsConversationsAsProducerDtoList;
    protected List<Conversation> usersConversationsAsInterestedPartyList;
    protected List<ConversationDto> usersConversationsAsInterestedPartyDtoList;
    protected List<Conversation> adminsConversationsAsInterestedPartyList;
    protected List<ConversationDto> adminsConversationsAsInterestedPartyDtoList;


    // OTHER REQUIRED COLLECTIONS
    protected Collection grantedAuthorities;

    // MISC

    protected List<Demo> danceDemoList;
    protected List<DemoDto> danceDemoDtoList;

    public void reInitializeInMemoryDatabase() {

//        IN THIS METHOD, WE SET UP THE IN MEMORY DATABASE, MAKING SURE TO AVOID CIRCULAR REFERENCES

        // INITIALIZE CONTEXT
        authentication = Mockito.mock(Authentication.class);
        securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // INITIALIZE HELPER OBJECTS
        earlierDate = Date.from(Instant.now());
        laterDate = Date.from(Instant.now());

        // INITIALIZE FILE SYSTEM OBJECTS
        primeAudioFileName = "PrimeAudio.mp3";
        audioSecundoFilename = "AudioSecundo.mp3";
        primeAudioMultipartFile = new MockMultipartFile(primeAudioFileName, new byte[]{0x01});
        audioSecundoMultipartFile = new MockMultipartFile(audioSecundoFilename, new byte[]{0x01});

        fileStorageLocation = "uploads";
        uploadPath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        primeAudioFilePath = Paths.get(fileStorageLocation + java.io.File.separator + primeAudioFileName);
        audioSecundoFilePath = Paths.get(fileStorageLocation + java.io.File.separator + audioSecundoFilename);

        primeAudioResource = new FileSystemResource(primeAudioFilePath);
        audioSecundoResource = new FileSystemResource(audioSecundoFilePath);

        //INITIALIZE AUTHORITIES
        userSimpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        adminSimpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        grantedAuthorities = Arrays.asList(userSimpleGrantedAuthority, adminSimpleGrantedAuthority);

        userAuthority = new Authority("user", "ROLE_USER");
        adminAuthority = new Authority("admin", "ROLE_ADMIN");

        userAuthoritySet = new HashSet<>();
        userAuthoritySet.add(userAuthority);

        adminAuthoritySet = new HashSet<>();
        adminAuthoritySet.add(userAuthority);
        adminAuthoritySet.add(adminAuthority);

        //INITIALIZE ENTITIES:
        dance = new Genre("Dance", null);
        rock = new Genre("Rock", null);

        user = new User("user", "12345", true, "fake key", "user@gmail.com", laterDate, userAuthoritySet, null, null, null, null);

        admin = new User("admin", "54321", true, "fake key", "admin@gmail.com", earlierDate, adminAuthoritySet, null, null, null, null);

        primeAudio = new Demo(1001L, earlierDate, "Prime Audio", 123D, 456D, null, null, null, null, null);

        audioSecundo = new Demo(1002L, earlierDate, "Audio Secundo", 456D, 123D, null, null, null, null, null);

        primeAudioFile = new AudioFile(3001, earlierDate, primeAudioFileName, null);
        audioSecundoFile = new AudioFile(3002, earlierDate, audioSecundoFilename, null);

        aboutPrimeAudio = new Conversation(2001L, earlierDate, earlierDate, "Re: Prime Audio", "body1", false, false, null, null, null);
        aboutAudioSecundo = new Conversation(2002L, earlierDate, earlierDate, "Re: Audio Secundo", "body2", false, false, null, null, null);

        demoCreatedEmail = new EmailDetails(null, "Prime Audio was created", "demoCreatedEmail body", null);
        sentMessageEmail = new EmailDetails(null, "A message has been sent to user user to alert him/her of your interest in demo Prime Audio ", "", null);


        // INITIALIZE DTOS
        userUserPublicDto = new UserPublicDto("user", earlierDate, Arrays.asList(primeAudio));
        adminUserPublicDto = new UserPublicDto("admin", laterDate, Arrays.asList(audioSecundo));
        primeAudioFileDto = AudioFileMapper.mapToDto(primeAudioFile);
        audioSecundoFileDto = AudioFileMapper.mapToDto(audioSecundoFile);
        userAuthorityDto = AuthorityMapper.mapToDto(userAuthority);
        adminAuthorityDto = AuthorityMapper.mapToDto(adminAuthority);
        aboutPrimeAudioDto = ConversationMapper.mapToDto(aboutPrimeAudio);
        aboutAudioSecundoDto = ConversationMapper.mapToDto(aboutAudioSecundo);
        primeAudioDto = DemoMapper.mapToDto(primeAudio);
        audioSecundoDto = DemoMapper.mapToDto(audioSecundo);
        demoCreatedEmailDto = EmailDetailsMapper.mapToDto(demoCreatedEmail);
        sentMessageEmailDto = EmailDetailsMapper.mapToDto(sentMessageEmail);
        danceDto = GenreMapper.mapToDto(dance);
        rockDto = GenreMapper.mapToDto(rock);
        userUserDto = UserMapper.mapToDto(user);
        adminUserDto = UserMapper.mapToDto(admin);

        // INITIALIZE LISTS
        allAudioFiles = Arrays.asList(primeAudioFile, audioSecundoFile);
        allAudioFileDtos = AudioFileMapper.mapToDto(allAudioFiles);
        allAuthorities = Arrays.asList(userAuthority, adminAuthority);
        allConversations = Arrays.asList(aboutPrimeAudio, aboutAudioSecundo);
        allConversationDtos = ConversationMapper.mapToDto(allConversations);
        allDemos = Arrays.asList(primeAudio, audioSecundo);
        allDemoDtos = DemoMapper.mapToDto(allDemos);
        allEmailDetailss = Arrays.asList(demoCreatedEmail, sentMessageEmail);
        allEmailDetailsDtos = EmailDetailsMapper.mapToDto(allEmailDetailss);
        allGenres = Arrays.asList(dance, rock);
        allGenreDtos = GenreMapper.mapToDto(allGenres);
        allUsers = Arrays.asList(user, admin);
        allUserDtos = UserMapper.mapToDto(allUsers);
        allUserPublicDtos = Arrays.asList(userUserPublicDto, adminUserPublicDto);

        usersProducedDemoList = Arrays.asList(primeAudio);
        usersProducedDemoDtoList = DemoMapper.mapToDto(usersProducedDemoList);
        adminsProducedDemoList = Arrays.asList(audioSecundo);
        adminsProducedDemoDtoList = DemoMapper.mapToDto(adminsProducedDemoList);
        usersFavoriteDemoList = Arrays.asList(audioSecundo);
        usersFavoriteDemoDtoList = DemoMapper.mapToDto(usersFavoriteDemoList);
        adminsFavoriteDemoList = Arrays.asList(primeAudio);
        adminsFavoriteDemoDtoList = DemoMapper.mapToDto(adminsFavoriteDemoList);
        usersConversationsAsProducerList = Arrays.asList(aboutPrimeAudio);
        usersConversationsAsProducerDtoList = ConversationMapper.mapToDto(usersConversationsAsProducerList);
        adminsConversationsAsProducerList = Arrays.asList(aboutAudioSecundo);
        adminsConversationsAsProducerDtoList = ConversationMapper.mapToDto(adminsConversationsAsProducerList);
        usersConversationsAsInterestedPartyList = Arrays.asList(aboutAudioSecundo);
        usersConversationsAsInterestedPartyDtoList = ConversationMapper.mapToDto(usersConversationsAsProducerList);
        adminsConversationsAsInterestedPartyList = Arrays.asList(aboutPrimeAudio);
        adminsConversationsAsInterestedPartyDtoList = ConversationMapper.mapToDto(adminsConversationsAsProducerList);

        // MISC
        danceDemoList = Arrays.asList(primeAudio);
        danceDemoDtoList = Arrays.asList(primeAudioDto);

    }
}

