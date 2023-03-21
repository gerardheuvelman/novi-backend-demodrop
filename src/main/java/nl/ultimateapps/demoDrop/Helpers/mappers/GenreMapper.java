package nl.ultimateapps.demoDrop.Helpers.mappers;

import jdk.dynalink.beans.StaticClass;
import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Models.Genre;
import nl.ultimateapps.demoDrop.Models.Genre;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GenreMapper {

    public static Genre mapToModel(GenreDto genreDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(genreDto, Genre.class);
    }

    public static List<Genre> mapToModel(List<GenreDto> genreDtoList) {
        ModelMapper modelMapper = new ModelMapper();
        List<Genre> genreList = new ArrayList<>();
        for (GenreDto genreDto : genreDtoList) {
            Genre genre = modelMapper.map(genreDto, Genre.class);
            genreList.add(genre);
        }
        return genreList;
    }

    public static GenreDto mapToDto(Genre genre) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(genre, GenreDto.class);
    }

    public static List<GenreDto> mapToDto(List<Genre> genreList) {
        ModelMapper modelMapper = new ModelMapper();
        List<GenreDto> genreDtoList = new ArrayList<>();
        for (Genre genre : genreList) {
            GenreDto genreDto = modelMapper.map(genre, GenreDto.class);
            genreDtoList.add(genreDto);
        }
        return genreDtoList;
    }

    public static List<GenreDto> mapToDto(Iterable<Genre> genreIterable) {
        List<Genre> genreList = StreamSupport.stream(genreIterable.spliterator(), false)
                .collect(Collectors.toList());
        return  mapToDto(genreList);
    }
}
