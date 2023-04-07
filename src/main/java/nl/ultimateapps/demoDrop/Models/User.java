package nl.ultimateapps.demoDrop.Models;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import lombok.*;
import nl.ultimateapps.demoDrop.Dtos.output.UserPrivateDto;
import nl.ultimateapps.demoDrop.Dtos.output.UserPublicDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserMapper;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
            cascade = CascadeType.ALL, // persist kan ook
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @Getter
    @Setter
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy = "producer", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @Getter
    @Setter
    private List<Demo> demos;

    @OneToMany(mappedBy = "initiator", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<Conversation> conversationsAsInitiator;

    @OneToMany(mappedBy = "correspondent", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<Conversation> conversationsAsCorrespondent;

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<UserReport> userReportsAsReporter;

    @OneToMany(mappedBy = "reportedUser", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<UserReport> userReportsAsReportedUser;

    @ManyToMany(mappedBy = "favoriteOfUsers" , cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<Demo> favoriteDemos;


    public UserPrivateDto toPrivateDto() {
        return UserMapper.mapToPrivateDto(this);
    }

    public UserPublicDto toPublicDto() {
        return UserMapper.mapToPublicDto(this);
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }

}