package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.ultimateapps.demoDrop.Models.Authority;
import java.util.Set;
import lombok.*;


public class UserDto {

    @Getter
    @Setter
    public String username;

    @Getter
    @Setter
    public String password;

    @Getter
    @Setter
    public Boolean enabled;

    @Getter
    @Setter
    public String apikey;

    @Getter
    @Setter
    public String email;

    //Relationships
    @JsonSerialize
    @Getter
    @Setter
    public Set<Authority> authorities;
}