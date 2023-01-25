package nl.ultimateapps.demoDrop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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

