package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.Models.AudioFile;
import nl.ultimateapps.demoDrop.Models.Demo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {

//    Optional<File> findByFileName(String fileName);

    Iterable<AudioFile> findAllByOrderByCreatedDateDesc();

}
