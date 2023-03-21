package nl.ultimateapps.demoDrop.Dtos.output;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AuthorityDto {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String authority;
}
