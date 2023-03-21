package nl.ultimateapps.demoDrop.Services;

import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.GenreMapper;
import nl.ultimateapps.demoDrop.Models.Genre;
import nl.ultimateapps.demoDrop.Repositories.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {GenreServiceImpl.class})
public class GenreServiceTest extends ServiceTest {

    @Mock
    protected GenreRepository genreRepository;

    @InjectMocks
    protected GenreServiceImpl genreServiceImpl;

    private final List<Genre> genreList = Arrays.asList(dance, rock);

    @Test
    @Disabled
    public void getGenresReturnAListOfGenreDtos() {

        //ARRANGE
        List<GenreDto> expectedGenreDtoList = Arrays.asList(danceDto, rockDto);

        //GIVEN
        Mockito.when(genreRepository.findAll()).thenReturn(genreList);

        //ACT
        //WHEN
        List<GenreDto> actualGenreDtoList = genreServiceImpl.getGenres();

        //ASSERT
        assertEquals(expectedGenreDtoList, actualGenreDtoList);

    }

    @Test
    @Disabled
    public void getGenreReturnAGenreDto() {

        //ARRANGE
        String genreName = dance.getName();
        GenreDto expectedGenreDto = danceDto;
        Genre genre = dance;

        //GIVEN
        Mockito.when(genreRepository.findById(genre.getName())).thenReturn(Optional.of(genre));

        //ACT
        //WHEN
        GenreDto actualGenreDto = genreServiceImpl.getGenre(genreName);

        //ASSERT
        assertEquals(expectedGenreDto, actualGenreDto);
    }

    @Test
    public void createGenreReturnsAGenreDtoWithTheCorrectName() {
        //ARRANGE
        String newGenreName = "Blues";
        GenreDto newGenreDto = new GenreDto(newGenreName, null);
        Genre genreTobeSaved = GenreMapper.mapToModel(newGenreDto);
        Genre savedGenre = genreTobeSaved;

        String expectedGenreName = newGenreName;

        //GIVEN
        Mockito.when(genreRepository.save(genreTobeSaved)).thenReturn(savedGenre);

        //ACT
        //WHEN
        String actualGenreName = genreServiceImpl.createGenre(newGenreDto);

        //ASSERT
        assertEquals(expectedGenreName, actualGenreName);
    }

    @Test
    public void DeleteGenresReturnsTheNumberOfDeletedGenres() {
        ///ARRANGE
        long expectedNumDeletedDemos = 2L;
        //GIVEN
        Mockito.when(genreRepository.findAll()).thenReturn(genreList);

        //ACT
        //WHEN
        long actualNumDeletedGenres = genreServiceImpl.deleteGenres();

        //ASSERT
        //THEN
        assertEquals(expectedNumDeletedDemos, actualNumDeletedGenres);
    }

    @Test
    @Disabled
    public void DeleteGenreReturnsTheNameOfTheDeletedGenre() {
        ///ARRANGE
        Genre genreToBeDeleted = dance;
        String expectedGenreName = genreToBeDeleted.getName();

        //GIVEN
        Mockito.when(genreRepository.findById(genreToBeDeleted.getName())).thenReturn(Optional.of(genreToBeDeleted));

        //ACT
        //WHEN
        String actualGenreName = genreServiceImpl.deleteGenre(genreToBeDeleted.getName());

        //ASSERT
        //THEN
        assertEquals(expectedGenreName, actualGenreName);
    }
}

