package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.Models.Demo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoRepository extends JpaRepository<Demo, Long> {

    public Iterable<Demo> findByTitleContaining(String brand);

}

