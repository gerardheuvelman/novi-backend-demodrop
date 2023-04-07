package nl.ultimateapps.demoDrop.Helpers.mappers;

import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GenreMapper {

    static boolean processing = false;

    public static Genre mapToModel(GenreDto genreDto) {
        if (processing) {
            return null;
        }
        processing = true;
        Genre genre = mapGenreDtoToGenre(genreDto);
        processing = false;
        return genre;
    }


    private static Genre mapGenreDtoToGenre(GenreDto genreDto) {
        Genre genre = new Genre();

        //normal fields:
        genre.setName(genreDto.getName());

        //Relationships (lists)
        genre.setDemos(genreDto.getDemos() != null ? DemoMapper.mapToModel(genreDto.getDemos()) : null);

        return genre;
    }

    public static List<Genre> mapToModel(List<GenreDto> genreDtoList) {
        List<Genre> genreList = new ArrayList<>();
        for (GenreDto genreDto : genreDtoList) {
            Genre genre = mapToModel(genreDto);
            genreList.add(genre);
        }
        return genreList;
    }

    public static GenreDto mapToDto(Genre genre) {
        if (processing) {
            return null;
        }
        processing = true;
        GenreDto genreDto = mapGenreToGenreDto(genre);
        processing = false;
        return genreDto;
    }


    private static GenreDto mapGenreToGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();

        //Normal fields
        genreDto.setName(genre.getName());

        // Relationships (lists)
        genreDto.setDemos(genre.getDemos() != null ? DemoMapper.mapToDto(genre.getDemos()): null);

        return genreDto;
    }

    public static List<GenreDto> mapToDto(List<Genre> genreList) {
        List<GenreDto> genreDtoList = new ArrayList<>();
        for (Genre genre : genreList) {
            GenreDto genreDto = mapToDto(genre);
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
