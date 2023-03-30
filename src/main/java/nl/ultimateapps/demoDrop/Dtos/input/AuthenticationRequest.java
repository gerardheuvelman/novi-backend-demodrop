package nl.ultimateapps.demoDrop.Dtos.input;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
public class AuthenticationRequest {

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
}