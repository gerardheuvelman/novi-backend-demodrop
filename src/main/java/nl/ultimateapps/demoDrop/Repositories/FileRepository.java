package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.Models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String> {

    Optional<File> findByFileName(String fileName);

}
