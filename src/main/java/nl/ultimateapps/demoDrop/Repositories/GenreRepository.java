package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.Models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;


    public interface GenreRepository extends JpaRepository<Genre, String> {

    }


