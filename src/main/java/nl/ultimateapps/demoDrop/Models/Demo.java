package nl.ultimateapps.demoDrop.Models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Demo {

    @Id
    @GeneratedValue
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "1004"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Getter
    private Long DemoId;

    @Getter
    @Setter
    private Date createdDate;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private Double length;

    @Getter
    @Setter
    private Double BPM;

    //Relationships:
    @OneToOne
    @JoinColumn(name = "file")
    @Getter
    @Setter
    private File file;

    @ManyToOne
    @JoinColumn(name = "genre")
    @Getter
    @Setter
    private Genre genre;

    //Relationships:
    @ManyToOne
    @JoinColumn(name = "username")
    @Getter
    @Setter
    private User user;

    @OneToMany(mappedBy = "demo", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<Conversation> conversations;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "demo_id"), inverseJoinColumns = @JoinColumn(name = "user_id"), name = "demos_users_favorites_")
    @Getter
    @Setter
    private List<User> favoriteOfUsers;
}