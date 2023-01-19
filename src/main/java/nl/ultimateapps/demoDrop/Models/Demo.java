package nl.ultimateapps.demoDrop.Models;

import javax.persistence.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Demo {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private Date createdDate;

    @Getter
    @Setter
    private Double length;

    @Getter
    @Setter
    private String audiofileUrl;

    //Relationships:
    @ManyToOne
    @JoinColumn(name = "username")  //optioneel. Dit is sowieso de default naam
    @Getter
    @Setter
    private User user;

    @OneToMany(mappedBy = "demo", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<Conversation> conversations;
}
