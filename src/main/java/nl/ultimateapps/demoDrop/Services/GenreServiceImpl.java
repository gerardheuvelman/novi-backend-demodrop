package nl.ultimateapps.demoDrop.Services;

import lombok.AllArgsConstructor;
import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Helpers.mappers.DemoMapper;
import nl.ultimateapps.demoDrop.Helpers.mappers.GenreMapper;
import nl.ultimateapps.demoDrop.Models.Genre;
import nl.ultimateapps.demoDrop.Repositories.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private GenreRepository genreRepository;

    @Override
    public List<GenreDto> getGenres() {
        Iterable<Genre> genreIterable = genreRepository.findAll();
        return GenreMapper.mapToDto(genreIterable);
    }

    @Override
    public GenreDto getGenre(String name) {
        if (genreRepository.findById(name).isPresent()) {
            Genre genre = genreRepository.findById(name).get();
            GenreDto genreDto = GenreMapper.mapToDto(genre);
            return genreDto;
        } else {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public String createGenre(GenreDto genreDto) {
        Genre genre = GenreMapper.mapToModel(genreDto);
        Genre savedGenre = genreRepository.save(genre);
        return savedGenre.getName();
    }

    @Override
    public long deleteGenres() {
        if (genreRepository.findAll() != null) {
            List<Genre> genres = genreRepository.findAll();
            long numDeletedGenres = 0;
            for (Genre genre : genres) {
                genreRepository.delete(genre);
                numDeletedGenres++;
            }
            return numDeletedGenres;
        } else {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public String deleteGenre(String name) {
        if (genreRepository.findById(name).isPresent()) {
            Genre genre = genreRepository.findById(name).get();
            String retrievedName = genre.getName();
            genreRepository.deleteById(name);
            return retrievedName;
        } else {
            throw new RecordNotFoundException();
        }
    }

}