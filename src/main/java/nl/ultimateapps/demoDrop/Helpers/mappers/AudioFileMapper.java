package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.AudioFileDto;
import nl.ultimateapps.demoDrop.Models.AudioFile;
import org.modelmapper.ModelMapper;

public class AudioFileMapper {
    public static AudioFile mapToModel(AudioFileDto audioFileDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(audioFileDto, AudioFile.class);
    }

    public static AudioFileDto mapToDto(AudioFile audioFile) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(audioFile, AudioFileDto.class);
    }
}
