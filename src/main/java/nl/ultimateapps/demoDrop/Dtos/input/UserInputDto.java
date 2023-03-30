package nl.ultimateapps.demoDrop.Dtos.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@AllArgsConstructor
public class UserInputDto {

    @NotBlank
    @Size(min=3, max=30)
    @Getter
    @Setter
    public String username;

    @NotBlank
    @Size(min=4, max=30)
    @Getter
    @Setter
    public String password;

    @NotBlank
    @Email
    @Getter
    @Setter
    public String email;
}
