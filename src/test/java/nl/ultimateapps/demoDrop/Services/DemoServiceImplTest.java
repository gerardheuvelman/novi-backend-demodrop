package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.Genre;
import nl.ultimateapps.demoDrop.Models.User;
import nl.ultimateapps.demoDrop.Repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DemoServiceImplTest {

    Date date;
    Genre  genre;
    User user;
    AudioFile audioFile;

    DemoDto demoDto1;

    DemoDto demoDto2;



    @Mock
    DemoRepository demoRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    GenreRepository genreRepository;

    @Mock
    AudioFileRepository audioFileRepository;

    @Mock
    AudioFileService audioFileService;

    @Mock
    EmailService emailService;

    @InjectMocks
    DemoServiceImpl demoServiceImpl;


//    public DemoServiceImplTest() {
//        this.demoServiceImpl = new DemoServiceImpl(demoRepository, userRepository , genreRepository, audioFileRepository, audioFileService, emailService );
//    }

    @BeforeEach
    void setUp() {

        date = new Date();
        genre = new Genre("Alternative Dance", null);
        user = new User("gerard", null, true, "fakeKey2", "gerardheuvelman@gmail.com", date, null, null, null, null,null);
        audioFile = new AudioFile(3001, date, "PrimeAudio.mp3", null);
        demoDto1 = new DemoDto(1001L, date, "Prime Audio",  123D, 123D , audioFile, genre, user , null, null );
        demoDto2 = new DemoDto(1002L, date, "AudioSecundo",  123D, 123D , audioFile, genre, user, null, null );

    }

    @Test
    void getDemosReturnsAListOfDemoDtos() {

        //ARRANGE

        List<DemoDto> expectedDemoDtoList = new ArrayList<>();
        expectedDemoDtoList.add(demoDto1);
        expectedDemoDtoList.add(demoDto2);

        Demo demo1 = new Demo(1001L, date, "Prime Audio",  123D, 123D , audioFile, genre, user , null, null );
        Demo demo2 = new Demo(1002L, date, "AudioSecundo",  123D, 123D , audioFile, genre, user, null, null );
        List<Demo> expectedDemoList = new ArrayList<>();
        expectedDemoList.add(demo1);
        expectedDemoList.add(demo2);

        Mockito.when(demoRepository.findAllByOrderByCreatedDateDesc()).thenReturn(expectedDemoList);

        //ACT
        List<DemoDto> actualDemoDtoList = demoServiceImpl.getDemos(0);

        //ASSERT
        assertEquals(expectedDemoDtoList, actualDemoDtoList);
    }
}