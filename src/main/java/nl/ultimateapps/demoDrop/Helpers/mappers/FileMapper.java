package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.FileDto;
import nl.ultimateapps.demoDrop.Models.File;
import org.modelmapper.ModelMapper;

public class FileMapper {
    public static File mapToModel(FileDto fileDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(fileDto, File.class);
    }

    public static FileDto mapToDto(File file) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(file, FileDto.class);
    }
}
