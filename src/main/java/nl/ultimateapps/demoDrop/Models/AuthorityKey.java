package nl.ultimateapps.demoDrop.Models;

import java.io.Serializable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AuthorityKey implements Serializable {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String authority;
}