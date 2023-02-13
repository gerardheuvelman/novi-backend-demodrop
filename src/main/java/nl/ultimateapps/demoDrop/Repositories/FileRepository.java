package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.Models.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<AudioFile, Long> {

//    Optional<File> findByFileName(String fileName);

}
