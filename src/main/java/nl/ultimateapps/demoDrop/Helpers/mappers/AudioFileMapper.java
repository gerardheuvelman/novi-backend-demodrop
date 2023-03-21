package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.AudioFileDto;
import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Models.Genre;
import org.modelmapper.ModelMapper;

import javax.persistence.metamodel.ListAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AudioFileMapper {
    public static AudioFile mapToModel(AudioFileDto audioFileDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(audioFileDto, AudioFile.class);
    }

    public static List<AudioFile> mapToModel(List<AudioFileDto> audioFileDtoList) {
        ModelMapper modelMapper = new ModelMapper();
        List<AudioFile> audioFileList = new ArrayList<>();
        for (AudioFileDto audioFileDto : audioFileDtoList) {
            AudioFile audioFile = modelMapper.map(audioFileDto, AudioFile.class);
            audioFileList.add(audioFile);
        }
        return audioFileList;
    }

    public static AudioFileDto mapToDto(AudioFile audioFile) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(audioFile, AudioFileDto.class);
    }

    public static List<AudioFileDto> mapToDto(List<AudioFile> audioFileList) {
        ModelMapper modelMapper = new ModelMapper();
        List<AudioFileDto> audioFileDtoList = new ArrayList<>();
        for (AudioFile audioFile : audioFileList) {
            AudioFileDto audioFileDto = modelMapper.map(audioFile, AudioFileDto.class);
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
