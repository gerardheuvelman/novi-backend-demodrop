package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    public Iterable<Conversation> findByCorrespondentOrderByLatestReplyDateDesc(User user);

    public Iterable<Conversation> findByInitiatorOrderByLatestReplyDateDesc(User user);

    public Iterable<Conversation> findAllByOrderByCreatedDateDesc(); // Conversations sorted by date descending


    public Iterable<Conversation> findAllByOrderByLatestReplyDateDesc(); // Conversations sorted by reply date descending


    public Iterable<Conversation> findByDemoOrderByLatestReplyDateDesc(Demo demo);

}
