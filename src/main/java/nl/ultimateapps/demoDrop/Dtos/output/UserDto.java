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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserDto {

    @NotBlank
    @Size(min=3, max=30)
    @Getter
    @Setter
    private String username;

    @NotBlank
    @Size(min=4, max=30)
    @Getter
    @Setter
    private String password;

    @NotNull
    @Getter
    @Setter
    private boolean enabled;

    @NotBlank
    @Getter
    @Setter
    private String apikey;

    @Email
    @Getter
    @Setter
    private String email;

    @NotNull
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