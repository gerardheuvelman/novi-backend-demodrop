package nl.ultimateapps.demoDrop.Models;

import javax.persistence.*;
import java.util.Date;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {

    @Id
    @GenericGenerator(
            name = "conversations-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "conversations_sequence"),
                    @Parameter(name = "initial_value", value = "2017"),
                    @Parameter(name = "increment_size", value = "1")
            })
    @GeneratedValue(generator = "conversations-sequence-generator")
    @Getter
    private Long ConversationId;

    @Getter
    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate;

    @Getter
    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date latestReplyDate;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String body;

    @Getter
    @Setter
    private boolean readByProducer;

    @Getter
    @Setter
    private boolean readByInterestedUser;

    //Relationships:
    @ManyToOne
    @JoinColumn(name = "Demo_id")  //optioneel. Dit is sowieso de default naam
    @Getter
    @Setter
    private Demo demo;

    @ManyToOne
    @JoinColumn(name = "producer_name")
    @Getter
    @Setter
    private User producer;

    @ManyToOne
    @JoinColumn(name = "interested_user_name")
    @Getter
    @Setter
    private User interestedUser;
}
