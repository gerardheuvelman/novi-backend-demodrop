package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.AudioFileDto;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AudioFileService {
    AudioFile processFileUpload(MultipartFile multipartFile);

    List<AudioFileDto> getAudioFiles(int limit);

    AudioFileDto getAudioFile(long audioFileId);

    long deleteAudioFile(long audioFileId);

    int deleteOrphanedMp3Files(String fileStorageLocation);

    Resource downloadMp3File(AudioFile audioFile);

}
