package nl.ultimateapps.demoDrop.Dtos.output;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AuthorityDto {


    @NotBlank
    @Size(min=3, max=30)
    @Getter
    @Setter
    private String username;

    @NotBlank
    @Getter
    @Setter
    private String authority;
}
