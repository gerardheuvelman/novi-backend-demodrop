package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.DemoMapper;
import nl.ultimateapps.demoDrop.Models.*;
import nl.ultimateapps.demoDrop.Repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {DemoServiceImpl.class})
class DemoServiceTest extends ServiceTest {

    @Mock
    private DemoRepository demoRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private AudioFileRepository audioFileRepository;

    @Mock
    private AudioFileService audioFileService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private DemoServiceImpl demoServiceImpl;

    @BeforeEach
    public void setUp() {
        // Re-initialize the entire in-memory database using the base class setup method.
        super.reInitializeInMemoryDatabase();
    }

    @Test
    void getDemosReturnsAListOfDemoDtos() {
        //ARRANGE
        List<DemoDto> expectedDemoDtoList = allDemoDtos;

        //GIVEN
        Mockito.when(demoRepository.findAllByOrderByCreatedDateDesc()).thenReturn(allDemos);

        //ACT
        //WHEN
        List<DemoDto> actualDemoDtoList = demoServiceImpl.getDemos(0);

        //ASSERT
        //THEN
        assertEquals(expectedDemoDtoList, actualDemoDtoList);
    }

    @Test
    void getPersonalDemosReturnsAListOfDemoDtos() {
        //ARRANGE
        User userToRetrievePersonalDemosFrom = user;
        List<DemoDto> expectedDemoDtoList = usersProducedDemoDtoList;

        //GIVEN
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(demoRepository.findByProducerOrderByCreatedDateDesc(user)).thenReturn(usersProducedDemoList);

        //ACT
        //WHEN
        List<DemoDto> actualDemoDtoList = demoServiceImpl.getPersonalDemos(user.getUsername());

        //ASSERT
        //THEN
        assertEquals(expectedDemoDtoList, actualDemoDtoList);
    }

    @Test
    void getFavoriteDemosReturnsAListOfDemoDtos() {
        //ARRANGE
        List<DemoDto> expectedDemoDtoList = usersFavoriteDemoDtoList;

        //GIVEN
        Mockito.when(demoRepository.findByFavoriteOfUsersOrderByTitleAsc(user)).thenReturn(usersFavoriteDemoList);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.of(user));
        // Fake a user with admin authority
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getAuthorities()).thenReturn(grantedAuthorities);

        //ACT
        //WHEN
        List<DemoDto> actualDemoDtoList = demoServiceImpl.getFavoriteDemos(user.getUsername());

        //ASSERT
        //THEN
        assertEquals(expectedDemoDtoList, actualDemoDtoList);
    }

    @Test
    void getDemosByGenreReturnsAListOfDemoDtos() {
        //ARRANGE
        Genre genre = dance;
        List<Demo> expectedDemoList = danceDemoList;
        List<DemoDto> expectedDemoDtoList = danceDemoDtoList;
        //GIVEN
        Mockito.when(demoRepository.findByGenreOrderByCreatedDateDesc(genre)).thenReturn(expectedDemoList);
        Mockito.when(genreRepository.findById(genre.getName())).thenReturn(Optional.of(genre));

        //ACT
        //WHEN
        List<DemoDto> actualDemoDtoList = demoServiceImpl.getDemosByGenre(genre.getName(), 0);

        //ASSERT
        //THEN
        assertEquals(expectedDemoDtoList, actualDemoDtoList);
    }

    @Test
    void getDemoReturnsADemoDto() {
        //ARRANGE
        Demo demo = primeAudio;
        long demoId = demo.getDemoId();
        DemoDto expectedDemoDto = primeAudioDto;
        //GIVEN

        Mockito.when(demoRepository.findById(demo.getDemoId())).thenReturn(Optional.of(demo));

        //ACT
        //WHEN
        DemoDto returnedDemoDto = demoServiceImpl.getDemo(demoId);

        //ASSERT
        //THEN
        assertEquals(expectedDemoDto, returnedDemoDto);
    }

    @Test
    public void CreateDemoReturnsADemoDto() throws UserPrincipalNotFoundException {
        //ARRANGE
        Demo newDemo = new Demo(1003L, earlierDate, "Third Demo", 123D, 456D, null, null, null, null, null, null);

        DemoDto postRequestBodyDemoDto = new DemoDto(null, earlierDate, "Third Demo", 123D, 456D, null, null, null, null, null, null);

        DemoDto expectedDemoDto = DemoMapper.mapToDto(newDemo);

        //GIVEN
        Mockito.when(userRepository.findById(authentication.getName())).thenReturn(Optional.of(user));
        Mockito.when(demoRepository.save(any(Demo.class))).thenReturn(newDemo);
        // Fake a user with admin authority
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);

        //ACT
        //WHEN
        DemoDto returnedDemoDto = demoServiceImpl.createDemo(postRequestBodyDemoDto);

        //ASSERT
        //THEN
        assertEquals(expectedDemoDto, returnedDemoDto);
    }

    @Test
    public void UpdateDemoReturnsADemoDto() throws UserPrincipalNotFoundException {

        // We assume that

        //ARRANGE
        Demo demo = primeAudio;
        User producer = user;
        demo.setProducer(producer);
        long demoId = demo.getDemoId();

        DemoDto putRequestBodyDemoDto = new DemoDto(null, earlierDate, "New Title", 123D, 456D, null, null, null, null, null, null);

        DemoDto expectedDemoDto = putRequestBodyDemoDto;
        expectedDemoDto.setDemoId(demoId);

        //GIVEN
        Mockito.when(demoRepository.findById(demo.getDemoId())).thenReturn(Optional.of(demo));
        Mockito.when(demoRepository.save(any(Demo.class))).thenReturn(demo);
        // Fake a user with admin authority
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getAuthorities()).thenReturn(grantedAuthorities);

        //ACT
        //WHEN
        DemoDto returnedDemoDto = demoServiceImpl.updateDemo(demoId,putRequestBodyDemoDto);

        //ASSERT
        //THEN
        assertEquals(expectedDemoDto.getTitle(), returnedDemoDto.getTitle());
    }

    @Test
    @Disabled
    public void setFavStatusReturnsABoolean() throws UserPrincipalNotFoundException {
        //ARRANGE
        Demo demo = primeAudio;
        demo.setFavoriteOfUsers(Arrays.asList(admin));
        Long demoId = primeAudio.getDemoId();
        boolean desiredStatus = true;
        boolean expectedStatus = true;

        //GIVEN
        Mockito.when(userRepository.findById(authentication.getName())).thenReturn(Optional.of(user));
        Mockito.when(demoRepository.findById(demo.getDemoId())).thenReturn(Optional.of(demo));
        Mockito.when(demoRepository.save(any(Demo.class))).thenReturn(demo);
        // Fake a user with admin authority
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);

        //ACT
        //WHEN
        boolean actualStatus = demoServiceImpl.setFavStatus(demoId, desiredStatus);

        //ASSERT
        //THEN
        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void assignGenreToDemoReturnsADemoDto() {
        //ARRANGE
        Demo demo = primeAudio;
        demo.setProducer(user);
        demo.setGenre(dance);
        long demoId = demo.getDemoId();

        Genre newGenre = rock;

        DemoDto demoDto = primeAudioDto;
        demoDto.setGenre(newGenre.toDto());

        DemoDto expectedDemoDto = primeAudioDto;
        expectedDemoDto.setGenre(newGenre.toDto());

        //GIVEN
        Mockito.when(demoRepository.findById(demoId)).thenReturn(Optional.of(demo));
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn(user.getUsername());
        Mockito.when(genreRepository.findById(newGenre.getName())).thenReturn(Optional.of(newGenre));

        //ACT
        //WHEN
        DemoDto actualDemoDto = demoServiceImpl.assignGenreToDemo(demoId, newGenre.getName());
        //ASSERT
        //THEN
        assertEquals(expectedDemoDto.getGenre(), actualDemoDto.getGenre());
    }


    @Test
    public void deleteDemosReturnsALong() {
        //ARRANGE
        long expectedDNumDeletedDemos = 2L;
        //GIVEN
        Mockito.when(demoRepository.findAll()).thenReturn(allDemos);

        //ACT
        //WHEN
        long actualNumDeletedDemos = demoServiceImpl.deleteDemos();

        //ASSERT
        //THEN
        assertEquals(expectedDNumDeletedDemos, actualNumDeletedDemos);
    }

    @Test
    public void deleteDemoReturnsADemoId() {
        //ARRANGE

        Demo demo = primeAudio;
        demo.setProducer(user);
        long demoId = demo.getDemoId();
        long expectedDemoId = demoId;
        //GIVEN
        Mockito.when(demoRepository.findById(demoId)).thenReturn(Optional.of(demo));
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn(user.getUsername());

        //ACT
        //WHEN
        long actualDemoId = demoServiceImpl.deleteDemo(expectedDemoId);

        //ASSERT
        //THEN
        assertEquals(expectedDemoId, actualDemoId);
    }

    @Test
    public void getDemoContainingReturnsAListOfDemoDtos() {
        //ARRANGE
        String query = "Audio"; // two DemoDtos should be returned
        Iterable<Demo> demoIterable = allDemos::iterator;

        List<DemoDto> expectedDemoDtoList = allDemoDtos;

        //GIVEN
        Mockito.when(demoRepository.findByTitleContainingIgnoreCase(query)).thenReturn(demoIterable);

        //ACT
        //WHEN
        List<DemoDto> actualDemoDtoList = demoServiceImpl.getDemosContaining(query);

        //ASSERT
        //THEN
        assertEquals(expectedDemoDtoList, actualDemoDtoList);
    }

    @Test
    @Disabled
    public void uploadFileAndAssignToDemoReturnsTrue() {
        //ARRANGE
        Demo demo = primeAudio;
        User producer = user;
        demo.setProducer(producer);
        long demoId = demo.getDemoId();
        AudioFile audioFile = primeAudioFile;
        long audioFileId = primeAudioFile.getAudioFileId();
        MultipartFile multipartFile = primeAudioMultipartFile;

        boolean expectedResult = true;
        //GIVEN
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(demoRepository.findById(demoId)).thenReturn(Optional.of(demo));
        Mockito.when(authentication.getName()).thenReturn(user.getUsername());
        Mockito.when(audioFileRepository.findById(audioFileId)).thenReturn(Optional.of(audioFile));
        Mockito.when(audioFileService.processFileUpload(multipartFile)).thenReturn(audioFile);
        Mockito.when(audioFileService.processFileUpload(multipartFile)).thenReturn(audioFile);
        Mockito.when(demoServiceImpl.assignFileToDemo(audioFile.getAudioFileId(), demo.getDemoId())).thenReturn(true);

        //ACT
        //WHEN
        boolean actualResult = demoServiceImpl.uploadFileAndAssignToDemo(demo.getDemoId(), multipartFile);

        //ASSERT
        //THEN
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void AssignFileToDemoReturnsTrue() {

        //ARRANGE
        Demo demo = primeAudio;
        AudioFile audioFile = primeAudioFile;
        audioFile.setDemo(demo);

        User producer = user;
        demo.setProducer(producer);

        boolean expectedResult = true;

        //GIVEN
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn(user.getUsername());
        Mockito.when(demoRepository.findById(audioFile.getDemo().getDemoId())).thenReturn(Optional.of(demo));
        Mockito.when(audioFileRepository.findById(audioFile.getAudioFileId())).thenReturn(Optional.of(audioFile));


        //ACT
        //WHEN
        boolean actualResult = demoServiceImpl.assignFileToDemo(audioFile.getAudioFileId(), demo.getDemoId());

        //ASSERT
        //THEN
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void downloadMp3FileReturnsAResource() {

        //ARRANGE
        Demo demo = primeAudio;
        demo.setAudioFile(primeAudioFile);

        Resource expectedResource = primeAudioResource;


        //GIVEN
        Mockito.when(demoRepository.findById(demo.getDemoId())).thenReturn(Optional.of(demo));
        Mockito.when(audioFileService.downloadMp3File(demo.getAudioFile())).thenReturn(primeAudioResource);

        //ACT
        //WHEN
        Resource actualResource = demoServiceImpl.downloadMp3File(demo.getDemoId());

        //ASSERT
        //THEN
        assertEquals(expectedResource, actualResource);

    }
}