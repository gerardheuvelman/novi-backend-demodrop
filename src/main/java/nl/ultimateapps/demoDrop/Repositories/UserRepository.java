package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
