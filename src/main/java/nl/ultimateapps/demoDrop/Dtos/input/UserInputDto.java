package nl.ultimateapps.demoDrop.Dtos.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
public class UserInputDto {

    @Getter
    @Setter
    public String username;

    @Getter
    @Setter
    public String password;

    @Getter
    @Setter
    public String email;
}
