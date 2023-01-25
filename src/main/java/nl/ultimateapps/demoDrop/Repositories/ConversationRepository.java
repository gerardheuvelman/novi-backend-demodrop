package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    public Iterable<Conversation> findByInterestedUserOrderByLatestReplyDateDesc(User user);

    public Iterable<Conversation> findByDemoOrderByLatestReplyDateDesc(Demo demo);

}
