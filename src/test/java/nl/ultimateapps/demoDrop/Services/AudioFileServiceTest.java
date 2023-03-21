package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.AudioFileDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.AudioFileMapper;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Repositories.AudioFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {AudioFileService.class})
public class AudioFileServiceTest extends ServiceTest {

    @Mock
    private AudioFileRepository audioFileRepository;

    @InjectMocks
    private AudioFileServiceImpl audioFileServiceImpl;

    @BeforeEach
    public void setUp() {
        // Re-initialize the entire in-memory database using the base class setup method.
        super.reInitializeInMemoryDatabase();
    }

    @Test
    @Disabled
    public void processFileUploadReturnsTheCorrectAudioFile() {
        //ARRANGE
        MultipartFile multipartFile = primeAudioMultipartFile;
        MultipartFile expectedMultipartFile = primeAudioMultipartFile;
        // GIVEN

        //ACT
        AudioFile actualMultiPartFile = audioFileServiceImpl.processFileUpload(multipartFile);

        //ASSERT
        assertEquals(expectedMultipartFile, actualMultiPartFile);

    }

    @Test
    @Disabled
    public void GetAudioFilesReturnAListOfAudioFileDtos() {
        //ARRANGE
        List<AudioFileDto> expectedAudioFileDtoList = allAudioFileDtos;

        //GIVEN
        Mockito.when(audioFileRepository.findAllByOrderByCreatedDateDesc()).thenReturn(allAudioFiles);

        //ACT
        //WHEN
        List<AudioFileDto> actualAudioFileDtoList = audioFileServiceImpl.getAudioFiles(0);

        //ASSERT
        //THEN
        assertEquals(expectedAudioFileDtoList, actualAudioFileDtoList);
    }

    @Test
    @Disabled
    public void GetAudioFileReturnsAAudioFileDto() {
        //ARRANGE
        AudioFile audioFile = primeAudioFile;
        long audioFileId = audioFile.getAudioFileId();

        AudioFileDto expectedAudioFileDto = primeAudioFileDto;

        //GIVEN
        Mockito.when(audioFileRepository.findById(audioFile.getAudioFileId())).thenReturn(Optional.of(audioFile));

        //ACT
        //WHEN
        AudioFileDto actualAudioFileDto = audioFileServiceImpl.getAudioFile(audioFileId);


        //ASSERT
        //THEN
        assertEquals(expectedAudioFileDto, actualAudioFileDto);
    }

    @Test
    @Disabled
    public void updateAudioFileReturnsTheCorrectAudioFileDto() {

        //ARRANGE
        AudioFile audioFile = primeAudioFile;

        AudioFileDto inputAudioFileDto = primeAudioFileDto;

        AudioFileDto expectedAudioFileDto = AudioFileMapper.mapToDto(audioFile);

        //GIVEN
        Mockito.when(audioFileRepository.findById(audioFile.getAudioFileId())).thenReturn(Optional.of(audioFile));
        Mockito.when(audioFileRepository.save(any(AudioFile.class))).thenReturn(audioFile);

        //ACT
        AudioFileDto actualAudioFileDto = audioFileServiceImpl.editAudioFile(audioFile.getAudioFileId(), inputAudioFileDto);
        //ASSERT
        assertEquals(expectedAudioFileDto, actualAudioFileDto);
    }

    @Test
    @Disabled
    public void deleteAudioFileReturnsTheIdOfTheDeletedAudioFile() {
        //ARRANGE
        AudioFile audioFile = primeAudioFile;

        long expectedAudioFileId = audioFile.getAudioFileId();

        //GIVEN
        Mockito.when(audioFileRepository.findById(audioFile.getAudioFileId())).thenReturn(Optional.of(audioFile));

        //ACT
        long actualAudioFileId = audioFileServiceImpl.deleteAudioFile(audioFile.getAudioFileId());


        //ASSERT
        assertEquals(expectedAudioFileId, actualAudioFileId);
    }
}
