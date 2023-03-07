package nl.ultimateapps.demoDrop.Dtos.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import nl.ultimateapps.demoDrop.Models.*;

import java.util.Date;
import java.util.List;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class DemoDto {

    @Getter
    @Setter
    private Long demoId;

    @Getter
    @Setter
    private Date createdDate;

//    @NotNull // MOET VALIDATIE HIER OF IN DE MODEL KLASSE? (ik denk hier in de DTO)
//    @Size(min = 2, max = 120)
    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private Double length;

    @Getter
    @Setter
    private Double bpm;

    //Relationships:
    @JsonIncludeProperties({"audioFileId", "originalFileName"})
    @Getter
    @Setter
    private AudioFile audioFile;

    @JsonIncludeProperties({"name"})
    @Getter
    @Setter
    private Genre genre;

    // Relationships:
    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private User user;

    @JsonIgnore
    @Getter
    @Setter
    private List<Conversation> conversations;

    @JsonIncludeProperties({"username"})
    @Getter
    @Setter
    private List<User> favoriteOfUsers;
}