package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.Setter;
import nl.ultimateapps.demoDrop.Models.Demo;
import java.util.Date;
import java.util.List;

public class UserPublicDto {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private Date createdDate;

    //Relationships:
    @JsonIncludeProperties({"demoId", "createdDate", "title", "length", "bpm", "genre"})
    @Getter
    @Setter
    private List<Demo> demos;
}
