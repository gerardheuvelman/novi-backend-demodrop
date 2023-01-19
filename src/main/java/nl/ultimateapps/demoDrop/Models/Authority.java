package nl.ultimateapps.demoDrop.Models;

import javax.persistence.*;
import java.io.Serializable;
import lombok.*;

@Entity
@IdClass(AuthorityKey.class)
@Table(name = "authorities")
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements Serializable {

    @Id
    @Column(nullable = false)
    @Getter
    @Setter
    private String username;

    @Id
    @Column(nullable = false)
    @Getter
    @Setter
    private String authority;
}
