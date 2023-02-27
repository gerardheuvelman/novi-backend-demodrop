package nl.ultimateapps.demoDrop.Models;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String username;

    @Column(nullable = false, length = 255)
    @Getter
    @Setter
    private String password;

    @Column(nullable = false)
    @Getter
    @Setter
    private boolean enabled = true;

    @Column
    @Getter
    @Setter
    private String apikey;

    @Column
    @Getter
    @Setter
    private String email;

    @Column
    @Getter
    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate;

    //Relationships:
    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "username",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)

    @Getter
    @Setter
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<Demo> demos;

    @OneToMany(mappedBy = "producer", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<Conversation> conversationsAsProducer;

    @OneToMany(mappedBy = "interestedUser", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<Conversation> conversationsAsInterestedParty;


    @ManyToMany(mappedBy = "favoriteOfUsers" , cascade = CascadeType.DETACH)
    private List<Demo> favoriteDemos;

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }

}