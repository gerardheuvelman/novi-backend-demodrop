package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Repositories.ConversationRepository;
import nl.ultimateapps.demoDrop.Repositories.DemoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class DemoServiceTest {
    @Mock
    DemoRepository demoRepository;
    @Mock
    ConversationRepository conversationRepository;

    //@InjectMocks kan ook
    DemoService demoService;

    @BeforeEach
    void setUp() {
        demoService = new DemoService(demoRepository, conversationRepository);
    }

    @Test
    void getDemosReturnsAListOfDemoDtos() throws MalformedURLException {
        //ARRANGE

        Date date = new Date();
        URL url = new URL("www.nu.nl/");
        DemoDto demoDto1 = new DemoDto("Prime Audio", date , 3.30, url);
        DemoDto demoDto2 = new DemoDto("Audio Secundo", date , 4.40, url);

        List<DemoDto> expectedDemoDtoList = new ArrayList<>();
        expectedDemoDtoList.add(demoDto1);
        expectedDemoDtoList.add(demoDto2);

        Demo demo1 = new Demo(1001L,"Prime Audio", date , 3.30, url, null, null);
        Demo demo2 = new Demo(1002L,"Audio Secundo", date , 4.40, url, null, null);

        List<Demo> expectedDemoList = new ArrayList<>();
        expectedDemoList.add(demo1);
        expectedDemoList.add(demo2);

        Mockito.when(demoRepository.findAll()).thenReturn(expectedDemoList);

        //ACT
        List<DemoDto> actualDemoDtoList = demoService.getDemos(0);

        //ASSERT
        assertEquals(expectedDemoDtoList, actualDemoDtoList);
    }
}