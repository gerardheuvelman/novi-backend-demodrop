package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;

import java.util.ArrayList;
import java.util.List;

public interface GenreService {
    List<GenreDto> getGenres();

    GenreDto getGenre(String name);

    String createGenre(GenreDto genreDto);

    long deleteGenres();

    String deleteGenre(String name);
}
