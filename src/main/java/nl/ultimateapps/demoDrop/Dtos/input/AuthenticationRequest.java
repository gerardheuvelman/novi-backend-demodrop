package nl.ultimateapps.demoDrop.Dtos.input;

import lombok.*;

@AllArgsConstructor
public class AuthenticationRequest {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;
}