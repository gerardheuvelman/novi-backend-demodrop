package nl.ultimateapps.demoDrop.Dtos.output;

import lombok.*;
@AllArgsConstructor
@EqualsAndHashCode
public class AuthenticationResponse {

    @Getter
    @Setter
    private String jwt;
}