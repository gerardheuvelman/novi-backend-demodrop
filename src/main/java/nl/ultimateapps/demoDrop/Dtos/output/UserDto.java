package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.ultimateapps.demoDrop.Models.Authority;

import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.*;
import nl.ultimateapps.demoDrop.Models.Conversation;
import nl.ultimateapps.demoDrop.Models.Demo;

import javax.persistence.Column;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserDto {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private boolean enabled;

    @Getter
    @Setter
    private String apikey;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private Date createdDate;


    //Relationships:
    @JsonIncludeProperties({"authority"})
    @Getter
    @Setter
    private Set<Authority> authorities;

    @JsonIncludeProperties({"title"})
    @Getter
    private List<Demo> demos;

    @JsonIgnore
    @Getter
    private List<Conversation> conversationsAsInterestedParty;

    @JsonIgnore
    @Getter
    private List<Demo> favoriteDemos;
}