package nl.ultimateapps.demoDrop.Dtos.output;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@EqualsAndHashCode
public class AuthenticationResponse {

    @NotBlank
    @Getter
    @Setter
    private String jwt;
}