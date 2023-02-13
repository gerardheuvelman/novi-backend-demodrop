package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DemoRepository extends JpaRepository<Demo, Long> {

    public Iterable<Demo> findByFavoriteOfUsersOrderByTitleAsc(User user);

    public Iterable<Demo> findByTitleContaining(String brand);

    public Iterable<Demo> findAllByOrderByCreatedDateDesc(); // Demos sorted by date descending


    public Iterable<Demo> findByUserOrderByCreatedDateDesc(User user); // Personal Demo list

    public Optional<Demo> findTopByOrderByCreatedDateDesc(); // voor de Hero sectie van de Front end
}

