package nl.ultimateapps.demoDrop.Models;

import javax.persistence.*;
import java.util.Date;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "2004"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Getter
    private Long ConversationId;

    @Getter
    @Setter
    private Date createdDate;

    @Getter
    @Setter
    private Date latestReplyDate;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String body;

    //Relationships:
    @ManyToOne
    @JoinColumn(name = "Demo_id")  //optioneel. Dit is sowieso de default naam
    @Getter
    @Setter
    private Demo demo;

    @ManyToOne
    @JoinColumn(name = "username")
    @Getter
    @Setter
    private User interestedUser;
}
