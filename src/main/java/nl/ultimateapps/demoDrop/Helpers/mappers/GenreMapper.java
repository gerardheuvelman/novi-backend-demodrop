package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Models.Genre;
import org.modelmapper.ModelMapper;

public class GenreMapper {
    public static Genre mapToModel(GenreDto genreDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(genreDto, Genre.class);

    }

    public static GenreDto mapToDto(Genre genre) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(genre, GenreDto.class);
    }
}
