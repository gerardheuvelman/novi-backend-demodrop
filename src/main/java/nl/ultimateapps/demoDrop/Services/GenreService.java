package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;

import java.util.ArrayList;

public interface GenreService {
    ArrayList<GenreDto> getGenres();

    GenreDto getGenre(String name);

    String createGenre(GenreDto genreDto);

    long deleteGenres();

    String deleteGenre(String name);

    String renameGenre(String oldGenreName, GenreDto genreDto);
}
