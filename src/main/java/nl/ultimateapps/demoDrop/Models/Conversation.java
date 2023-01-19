package nl.ultimateapps.demoDrop.Models;

import javax.persistence.*;
import java.util.Date;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

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
