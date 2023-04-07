package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;
import nl.ultimateapps.demoDrop.Helpers.mappers.UserMapper;
import nl.ultimateapps.demoDrop.Models.Demo;
import nl.ultimateapps.demoDrop.Models.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserPublicDto {

    @NotBlank
    @Size(min=3, max=30)
    @Getter
    @Setter
    private String username;

    @NotNull
    @Getter
    @Setter
    private Date createdDate;

    //Relationships:
    @JsonIncludeProperties({"demoId", "createdDate", "title", "length", "bpm", "genre"})
    @Getter
    @Setter
    private List<DemoDto> demos;

    public User toModel() {
        return UserMapper.mapToModel(this);
    }

}
