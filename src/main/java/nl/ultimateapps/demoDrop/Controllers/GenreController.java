package nl.ultimateapps.demoDrop.Controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Exceptions.RecordNotFoundException;
import nl.ultimateapps.demoDrop.Services.GenreServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {

    @Getter
    @Setter
    private GenreServiceImpl genreService;

    @GetMapping("")
    public ResponseEntity<ArrayList<GenreDto>> getGenres() {
        ArrayList<GenreDto> genreDtos = genreService.getGenres();
        if (genreDtos.size()>0) {
            return ResponseEntity.ok(genreDtos);
        } else {
            throw new RecordNotFoundException("No Genres found");
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<GenreDto> getGenre (@PathVariable String name) {
        GenreDto genreDto = genreService.getGenre(name);
        return ResponseEntity.ok(genreDto);
    }

    @PostMapping("")
    public ResponseEntity<String> postGenre(@RequestBody GenreDto genreDto) {
        String savedGenre = genreService.createGenre(genreDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/genres/" + savedGenre).toUriString());
        return ResponseEntity.created(uri).body("Genre created!");
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteGenres() {
        long numDeletedGenres = genreService.deleteGenres();
        return ResponseEntity.ok(numDeletedGenres + " genres deleted successfully.");
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteGenre(@PathVariable String name) {
        String deletedGenreName = genreService.deleteGenre(name);
        return ResponseEntity.ok("Genre "+ deletedGenreName + " was deleted successfully.");
    }
}
