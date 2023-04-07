package nl.ultimateapps.demoDrop.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import nl.ultimateapps.demoDrop.Dtos.output.DemoDto;
import nl.ultimateapps.demoDrop.Dtos.output.GenreDto;
import nl.ultimateapps.demoDrop.Helpers.mappers.DemoMapper;
import nl.ultimateapps.demoDrop.Helpers.mappers.GenreMapper;

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

    public GenreDto toDto() {
        return GenreMapper.mapToDto(this);
    }
}

