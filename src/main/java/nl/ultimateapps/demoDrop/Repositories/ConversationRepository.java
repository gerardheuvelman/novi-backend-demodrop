package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.Models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

}
