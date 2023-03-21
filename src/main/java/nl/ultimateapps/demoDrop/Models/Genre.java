package nl.ultimateapps.demoDrop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Genre {

    @Id
    @Getter
    @Setter
    private String name;

    //Relationships:
    @JsonIgnore
    @OneToMany(mappedBy = "genre", cascade = CascadeType.REMOVE)
    @Getter
    @Setter
    private List<Demo> demos;
}

