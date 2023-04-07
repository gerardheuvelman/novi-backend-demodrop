package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.AudioFileDto;
import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Models.Demo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AudioFileMapper {
    static boolean processing = false;


    public static AudioFile mapToModel(AudioFileDto audioFileDto) {
        if (processing) {
            return null;
        }
        processing = true;
        AudioFile audioFile = mapAudioFileDtoToAudioFile(audioFileDto);
        processing = false;
        return audioFile;
    }

    private static AudioFile mapAudioFileDtoToAudioFile(AudioFileDto audioFileDto) {
        AudioFile audioFile = new AudioFile();

        // normal fields:
        audioFile.setAudioFileId(audioFileDto.getAudioFileId());
        audioFile.setCreatedDate(audioFileDto.getCreatedDate());
        audioFile.setOriginalFileName(audioFileDto.getOriginalFileName());

        // Relationships (objects):
        audioFile.setDemo(audioFileDto.getDemo() != null ? audioFileDto.getDemo().toModel(): null);

        return audioFile;
    }

    public static List<AudioFile> mapToModel(List<AudioFileDto> audioFileDtoList) {
        List<AudioFile> audioFileList = new ArrayList<>();
        for (AudioFileDto audioFileDto : audioFileDtoList) {
            AudioFile audioFile = mapToModel(audioFileDto);
            audioFileList.add(audioFile);
        }
        return audioFileList;
    }

    public static AudioFileDto mapToDto(AudioFile audioFile) {
        if (processing) {
            return null;
        }
        processing = true;
        AudioFileDto audioFileDto = mapAudioFileToAudioFileDto(audioFile);
        processing = false;
        return audioFileDto;
    }

    public static AudioFileDto mapAudioFileToAudioFileDto(AudioFile audioFile) {
        AudioFileDto audioFileDto = new AudioFileDto();

        //Normal fields
        audioFileDto.setAudioFileId(audioFile.getAudioFileId());
        audioFileDto.setCreatedDate(audioFile.getCreatedDate());
        audioFileDto.setOriginalFileName(audioFile.getOriginalFileName());
        // Relationships (objects)
        audioFileDto.setDemo(audioFile.getDemo() != null ? audioFile.getDemo().toDto(): null);

        return audioFileDto;
    }

    public static List<AudioFileDto> mapToDto(List<AudioFile> audioFileList) {
        List<AudioFileDto> audioFileDtoList = new ArrayList<>();
        for (AudioFile audioFile : audioFileList) {
            AudioFileDto audioFileDto =mapToDto(audioFile);
            audioFileDtoList.add(audioFileDto);
        }
        return audioFileDtoList;
    }

    public static List<AudioFileDto> mapToDto(Iterable<AudioFile> audioFileIterable) {
        List<AudioFile> audioFileList = StreamSupport.stream(audioFileIterable.spliterator(), false)
                .collect(Collectors.toList());
        return mapToDto(audioFileList);
    }
}
