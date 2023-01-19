package nl.ultimateapps.demoDrop.Dtos.output;

import lombok.*;
@AllArgsConstructor
public class AuthenticationResponse {

    @Getter
    @Setter
    private String jwt;
}