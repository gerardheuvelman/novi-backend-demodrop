package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import nl.ultimateapps.demoDrop.Helpers.mappers.GenreMapper;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserMapper;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.Genre;
import nl.ultimateapps.demoDrop.Models.User;

import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GenreDto {

    @NotBlank
    @Getter
    @Setter
    private String name;

    //Relationships:

    @JsonIgnore
    @Getter
    @Setter
    private List<DemoDto> demos;

    public Genre toModel() {
        return GenreMapper.mapToModel(this);
    }

}
